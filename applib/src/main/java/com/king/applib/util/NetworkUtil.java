package com.king.applib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络相关工具类.
 * Created by HuoGuangxu on 2016/10/20.
 */

public class NetworkUtil {
    private NetworkUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 判断网络是否可用.
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 判断是否是wifi
     */
    public static boolean isWifi() {
        return ConnectivityManager.TYPE_WIFI == getNetWorkType();
    }

    /**
     * 获得当前网络类型.获取不到返回-1.
     */
    public static int getNetWorkType() {
        ConnectivityManager manager = (ConnectivityManager) ContextUtil.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                        return ConnectivityManager.TYPE_WIFI;
                    case ConnectivityManager.TYPE_MOBILE:
                        return ConnectivityManager.TYPE_MOBILE;
                    default:
                        return -1;
                }
            }
        }
        return -1;
    }
}
