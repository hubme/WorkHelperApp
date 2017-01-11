package com.king.app.workhelper.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.king.app.workhelper.constant.GlobalConstant;
import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.DateTimeUtil;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.FileUtil;
import com.king.applib.util.IOUtil;
import com.king.applib.util.SPUtil;
import com.king.applib.util.StringUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * 收集crash Log
 * created by VanceKing at 2016/12/11
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    //循环重启的阈(yu)值
    private final int TIMESTAMP_AVOID_RESTART_LOOPS_IN_MILLIS = 2000;

    private UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE;
    private WeakReference<Context> mContext;
    private static Class<? extends Activity> mCrashedActivity;
    /** 应用在后台是否crash */
    private static boolean mCrashInBackground = false;
    private String mLogSavedDir;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashHandler();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param logDir  日志保存目录
     */
    public void init(Context context, String logDir) {
        mContext = new WeakReference<>(context.getApplicationContext());
        mLogSavedDir = StringUtil.isNullOrEmpty(logDir) ? mContext.get().getCacheDir().getAbsolutePath() : logDir;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /** 当UncaughtException发生时会转入该函数来处理 */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Logger.e(throwable, "App crashed");
        saveCrashInfo(throwable);
        if (!mCrashInBackground && !AppManager.getInstance().isForeground()) {//后台拦截crash，对用户无感知
            return;
        }
        if (mCrashedActivity == null || hasCrashedInTheLastSeconds(mContext.get()) || isStackTraceLikelyConflictive(throwable, mCrashedActivity)) {
            Logger.i("CrashedActivity为null或应用crash循环重启,交给DefaultUncaughtExceptionHandler");
            //避免crash后启动的Activity也crash了，造成循环重启，或者卡死的现象。
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            saveLastCrashTimestamp(mContext.get(), System.currentTimeMillis());
            startCrashedActivity();
            killCurrentProcess();
        }
    }

    /** 设置Crash后显示的Activity */
    public static void setCrashedActivity(Class<? extends Activity> crashedActivity) {
        mCrashedActivity = crashedActivity;
    }

    /** 应用后台是否crash */
    public static void setCrashInBackground(boolean isCrash) {
        mCrashInBackground = isCrash;
    }

    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private boolean saveCrashInfo(final Throwable throwable) {
        if (!ExtendUtil.isSDCardAvailable()) {
            return false;
        }

        String fileName = "crash_" + DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd_HH-mm-ss") + "_" + System.currentTimeMillis() + ".log";
        final String fullPath = mLogSavedDir.endsWith(File.separator) ? mLogSavedDir + fileName : mLogSavedDir + File.separator + fileName;
        Logger.i("日志保存路径--->" + fullPath);

        File fileLog = FileUtil.createFile(fullPath);
        if (fileLog == null) {
            return false;
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(fileLog, false));
            printWriter.write(getCrashHead());
            throwable.printStackTrace(printWriter);
            Throwable cause = throwable.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            IOUtil.close(printWriter);
        }
    }

    // TODO 发送错误报告到服务器
    private void reportCrashLogs(String logPath) {
    }

    /**
     * 收集设备参数信息
     */
    private String getDeviceInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";

                sb.append("versionName: ").append(versionName).append("\n")
                        .append("versionCode: ").append(versionCode).append("\n");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.i("an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                sb.append(field.getName()).append(": ").append(field.get(null).toString()).append("\n");
            } catch (Exception e) {
                Logger.i("an error occured when collect crash info", e);
            }
        }
        return sb.append("\n").toString();
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        return "\n************* Crash Log Head ****************" +
                "\nApp VersionName    : " + AppUtil.getAppInfo(mContext.get()).getVersionName() +
                "\nApp VersionCode    : " + AppUtil.getAppInfo(mContext.get()).getVersionCode() +
                "\nApp PackageName    : " + AppUtil.getAppInfo(mContext.get()).getPackageName() +
                "\nAndroid Version    : " + Build.VERSION.RELEASE +
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                "\nMANUFACTURER       : " + Build.MANUFACTURER +
                "\nModel              : " + Build.MODEL +
                "\nBRAND              : " + Build.BRAND +
                "\nDEVICE             : " + Build.DEVICE +
                "\n************* Crash Log Head ****************\n\n";
    }

    private void saveLastCrashTimestamp(Context context, long timestamp) {
        SPUtil.putLong(context, GlobalConstant.SP_PARAMS_KEY.LAST_CRASH_TIMESTAMP, timestamp);
    }

    private long getLastCrashTimestamp(Context context) {
        return SPUtil.getLong(context, GlobalConstant.SP_PARAMS_KEY.LAST_CRASH_TIMESTAMP);
    }

    //是否符合循环启动的条件.可能CrashedActivity也crash了，造成循环启动。
    private boolean hasCrashedInTheLastSeconds(Context context) {
        long lastTimestamp = getLastCrashTimestamp(context);
        long currentTimestamp = System.currentTimeMillis();

        return lastTimestamp < currentTimestamp && currentTimestamp - lastTimestamp < TIMESTAMP_AVOID_RESTART_LOOPS_IN_MILLIS;
    }

    /*
    1.可能Application.onCreate()方法中crash了 (handleBindApplication is in the stack)
    2.CrashedActivity也crash了 (crashedClass is in the stack)
     */
    private static boolean isStackTraceLikelyConflictive(Throwable throwable, Class<? extends Activity> crashedClass) {
        do {
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (StackTraceElement element : stackTrace) {
                if (element == null) {
                    continue;
                }
                if (("android.app.ActivityThread".equals(element.getClassName()) && "handleBindApplication".equals(element.getMethodName()))
                        || (crashedClass != null && crashedClass.getName().equals(element.getClassName()))) {
                    return true;
                }
            }
        } while ((throwable = throwable.getCause()) != null);
        return false;
    }

    private void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private void startCrashedActivity() {
        if (mContext.get() == null || mCrashedActivity == null) {
            return;
        }
        Intent intent = new Intent(mContext.get(), mCrashedActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.get().startActivity(intent);
    }
}