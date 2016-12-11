package com.king.app.workhelper.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.king.applib.log.Logger;
import com.king.applib.util.AppUtil;
import com.king.applib.util.DateTimeUtil;
import com.king.applib.util.ExtendUtil;
import com.king.applib.util.FileUtil;
import com.king.applib.util.IOUtil;

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
    public final String TAG = this.getClass().getSimpleName();

    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE;
    private WeakReference<Context> mContext;

    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
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
     */
    public void init(Context context) {
        if (context == null) {
            return;
        }
        mContext = new WeakReference<>(context.getApplicationContext());

        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        /*if (mDefaultHandler != null && !handleException(ex)) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            exitApp();
        }*/
        handleException(ex);
        //交给DefaultHandler处理，否则错误Log在IDE打不出来
        mDefaultHandler.uncaughtException(thread, ex);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        // 发送错误报告到服务器
        //reportCrashLogs("");
        return saveCrashInfo(throwable);
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

        String fileName = "crash_" + DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd_HH-mm-ss")
                + "_" + System.currentTimeMillis() + ".log";
        final String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "000crash/" + fileName;
        Log.i(TAG, "日志保存路径--->" + fullPath);

        File fileLog = FileUtil.createFile(fullPath);
        if (fileLog == null) {
            return false;
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(fullPath, false));
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

    private void reportCrashLogs(String logPath) {
        // TODO 发送错误报告到服务器
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

    private void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
