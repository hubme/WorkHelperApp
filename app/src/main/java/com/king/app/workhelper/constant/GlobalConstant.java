package com.king.app.workhelper.constant;

import android.support.annotation.IntDef;

/**
 * 全局常量类
 * Created by HuoGuangxu on 2016/9/29.
 */

public class GlobalConstant {
    public interface BUNDLE_PARAMS_KEY {
        String EXTRA_KEY = "EXTRA_KEY";
    }

    public interface INTENT_PARAMS_KEY {
        String APP_DOWNLOAD_URL = "APP_DOWNLOAD_URL";
    }

    /** 未知网络 */
    public static final int TYPE_NONE = -1;
    /** See:{@link android.net.ConnectivityManager#TYPE_WIFI wifi网络} */
    public static final int TYPE_WIFI = 0;
    /** See:{@link android.net.ConnectivityManager#TYPE_MOBILE 手机网络} */
    public static final int TYPE_MOBILE = 1;

    @IntDef({TYPE_NONE, TYPE_WIFI, TYPE_MOBILE})
    public @interface NETWORK_TYPE {

    }
}