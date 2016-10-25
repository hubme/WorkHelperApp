package com.king.applib.constant;

/**
 * Created by HuoGuangxu on 2016/10/21.
 */

public class GlobalConstants {
    private GlobalConstants() {

    }

    //网络状态
    public interface NETWORK_TYPE {
        /** 未知网络 */
        int TYPE_UNKNOWN = -1;
        /** See:{@link android.net.ConnectivityManager#TYPE_WIFI wifi网络} */
        int TYPE_WIFI = 0;
        /** See:{@link android.net.ConnectivityManager#TYPE_MOBILE 手机网络} */
        int TYPE_MOBILE = 1;
    }

}
