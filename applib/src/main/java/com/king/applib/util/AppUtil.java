package com.king.applib.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * App相关工具类.
 * Created by HuoGuangXu on 2016/10/19.
 */
public class AppUtil {

    private AppUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 封装App信息的Bean类
     */
    public static class AppInfo {

        private String name;
        private Drawable icon;
        private String packageName;
        private String packagePath;
        private String versionName;
        private int versionCode;

        public String getName() {
            return name;
        }

        public Drawable getIcon() {
            return icon;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getPackagePath() {
            return packagePath;
        }

        public String getVersionName() {
            return versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        private AppInfo() {
            this.name = "";
            this.icon = null;
            this.packageName = "";
            this.packagePath = "";
            this.versionName = "";
            this.versionCode = 0;
        }

        /**
         * @param name        名称
         * @param icon        图标
         * @param packageName 包名
         * @param packagePath 包路径
         * @param versionName 版本号
         * @param versionCode 版本Code
         */
        private AppInfo(String name, Drawable icon, String packageName, String packagePath,
                        String versionName, int versionCode) {
            this.name = name;
            this.icon = icon;
            this.packageName = packageName;
            this.packagePath = packagePath;
            this.versionName = versionName;
            this.versionCode = versionCode;
        }

        @Override
        public String toString() {
            return "AppInfo{" +
                    "name='" + name + '\'' +
                    ", icon=" + icon +
                    ", packageName='" + packageName + '\'' +
                    ", packagePath='" + packagePath + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", versionCode=" + versionCode +
                    '}';
        }
    }

    public static AppInfo getAppInfo() {
        PackageManager pm = ContextUtil.getAppContext().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(ContextUtil.getAppContext().getPackageName(), 0);
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            String name = appInfo.loadLabel(pm).toString();
            Drawable icon = appInfo.loadIcon(pm);
            return new AppInfo(name, icon, packageInfo.packageName, appInfo.sourceDir, packageInfo.versionName, packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            return new AppInfo();
        }
    }

    /**
     * 清除本应用内部数据(/data/data/PackageName/)和外部数据(/Android/data/PackageName/)<br/>
     * 应用会自动关闭,下次打开和第一次安装效果一样.
     */
    public static boolean clearUserData() {
        ActivityManager activityManager = (ActivityManager) ContextUtil.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return activityManager.clearApplicationUserData();
        } else {
            return FileUtil.deleteDir(ContextUtil.getAppContext().getExternalCacheDir().getParent())
                    && FileUtil.deleteDir(ContextUtil.getAppContext().getCacheDir().getParent());

        }
    }

    /**
     * 安装apk(支持6.0)
     */
    public static void installApk(Context context, File file) {
        Intent intent = getInstallAppIntent(file);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 安装apk(支持6.0)
     */
    public static void installApk(Activity activity, File file, int requestCode) {
        Intent intent = getInstallAppIntent(file);
        if (intent != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 获取安装Intent
     */
    public static Intent getInstallAppIntent(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtil.getFileExtension(file));
        }
        intent.setDataAndType(Uri.fromFile(file), type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /** 获取当前进程名称 */
    public static String getCurrentProcessName(Context context) {
        if (context == null) {
            return "";
        }
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    /** 获取当前进程名称 */
    public static String getProcessName(Context context) {
        BufferedReader mBufferedReader = null;
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            mBufferedReader = new BufferedReader(new FileReader(file));
            return mBufferedReader.readLine().trim();
        } catch (Exception e) {
            return getCurrentProcessName(context);
        } finally {
            IOUtil.close(mBufferedReader);
        }
    }

    /** 获取Activity所在的进程名称 */
    public static String getActivityProcessName(Class<? extends Activity> cls) {
        try {
            ComponentName component = new ComponentName(ContextUtil.getAppContext(), cls);
            ActivityInfo activityInfo = ContextUtil.getAppContext().getPackageManager().getActivityInfo(component, 0);
            return activityInfo.processName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前应用FileProvide的author name。<br/>
     * 注意：不同应用使用相同的provider,安装时会出现INSTALL_FAILED_CONFLICTING_PROVIDER的错误
     */
    public static String getFileProviderAuthor() {
        String pkgName = AppUtil.getAppInfo().getPackageName();
        return StringUtil.isNullOrEmpty(pkgName) ? "com.fund.app.fileprovider" : pkgName + ".fileprovider";
    }
}