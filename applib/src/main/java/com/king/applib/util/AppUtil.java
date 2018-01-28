package com.king.applib.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
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
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.king.applib.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * App相关工具类.
 *
 * @author VanceKing
 * @since 2016/10/19.
 */
public class AppUtil {
    public static final String PKG_NAME_QQ = "com.tencent.mobileqq";
    public static final String PKG_NAME_WEIXIN = "com.tencent.mm";
    public static final String PKG_name_WEIBO = "com.sina.weibo";
    public static final String PKG_NAME_ALIPAY = "com.eg.android.AlipayGphone";

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
    public static boolean clearUserData(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return activityManager.clearApplicationUserData();
        } else {
            return FileUtil.deleteDir(ContextUtil.getAppContext().getExternalCacheDir().getParent())
                    && FileUtil.deleteDir(ContextUtil.getAppContext().getCacheDir().getParent());

        }
    }

    /**
     * 安装apk(支持7.0)
     */
    public static void installApk(Context context, File file) {
        Intent intent = getInstallAppIntent(context, file);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 安装apk(支持7.0)
     */
    public static void installApk(Activity activity, File file, int requestCode) {
        Intent intent = getInstallAppIntent(activity, file);
        if (intent != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 获取安装Intent
     */
    public static Intent getInstallAppIntent(Context context, File file) {
        if (context == null || file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri apkUri = FileProvider.getUriForFile(context, AppUtil.getFileProviderAuthor(), file);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        return intent;
    }

    /** 获取当前进程名称 */
    public static String getCurrentProcessName(Context context) {
        if (context == null) {
            return "";
        }
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
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

    /**
     * 根据Activity名称打开Activity.<br/>
     * 注意:Activity 的所在路径已写死，全路径一定要保持一致。否则打开失败.
     *
     * @param activity       上下文
     * @param shortClassName activity 短名称
     */
    public static boolean openActivity(Activity activity, String shortClassName) {
        try {
            Intent intent = new Intent();
            intent.setClassName(activity.getPackageName(), "com.king.app.workhelper.activity." + shortClassName);
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            Logger.e(Log.getStackTraceString(e));
            return false;
        }
    }

    /**
     * 根据类全名启动Activity
     *
     * @param context       上下文
     * @param fullClassPath Activity全名
     */
    public static boolean openActivity(Context context, String fullClassPath) {
        if (context == null || StringUtil.isNullOrEmpty(fullClassPath)) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        final String packageName = AppUtil.getAppInfo().getPackageName();
        try {
            ComponentName cn = new ComponentName(packageName, fullClassPath);
            intent.setComponent(cn);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean openThirdApp(Context context, String packageName) {
        if (context == null || StringUtil.isNullOrEmpty(packageName)) {
            return false;
        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /** 是否能正常唤醒intent */
    public static boolean canResolveActivity(Intent intent) {
        return intent != null && intent.resolveActivity(ContextUtil.getAppContext().getPackageManager()) != null;
    }

    /** 是否能正常唤醒Activity */
    public static boolean canResolveActivity(Context context, Intent intent) {
        return intent != null && intent.resolveActivityInfo(context.getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null;
    }

    /**
     * 打开能处理此uri的应用.tel:// http:// market://
     */
    public static boolean openAppByUri(Context context, String uri) {
        if (StringUtil.isNullOrEmpty(uri)) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取文件的MIME类型.
     */
    public static String getMIMEType(File file) {
        if (file == null || !file.isFile()) {
            return "";
        }

        final String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static boolean isAppClientInstall(Context context, String pkgName) {
        if (context == null || StringUtil.isNullOrEmpty(pkgName)) {
            return false;
        }
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        if (installedPackages == null || installedPackages.isEmpty()) {
            return false;
        }
        for (PackageInfo info : installedPackages) {
            if (info.packageName.equalsIgnoreCase(pkgName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClass) {
        if (context == null || serviceClass == null) {
            return false;
        }
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}