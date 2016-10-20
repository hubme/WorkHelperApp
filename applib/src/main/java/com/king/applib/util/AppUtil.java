package com.king.applib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * App相关工具类.
 * Created by HuoGuangXu on 2016/10/19.
 */
public class AppUtil {

    private AppUtil() {
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

        /**
         * @param name 名称
         * @param icon 图标
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

        @Override public String toString() {
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

    public static AppInfo getAppInfo(Context context) {
        if (context == null) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            String name = appInfo.loadLabel(pm).toString();
            Drawable icon = appInfo.loadIcon(pm);
            return new AppInfo(name, icon, packageInfo.packageName, appInfo.sourceDir, packageInfo.versionName, packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}