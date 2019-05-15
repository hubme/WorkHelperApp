
package com.king.applib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

public class DebugUtil {

    /**
     * 判断当前应用是否是可调试的
     */
    public static boolean isInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断某一应用是否是可调试的
     */
    public static boolean isAppDebugable(Context context, String packageName) {
        try {
            PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            if (pkgInfo != null) {
                ApplicationInfo info = pkgInfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
