package com.king.app.workhelper.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author huoguangxu
 * @since 2017/4/7.
 */

public class OkHttpUtil {
//    private OkHttpUtil mInstance;
    private static OkHttpClient mOkHttpClient;

    private OkHttpUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /*private static class HttpClientHolder {
        private static OkHttpUtil okHttpUtil = new OkHttpUtil();
    }

    public static OkHttpUtil getInstance() {
        return HttpClientHolder.okHttpUtil;
    }*/

    public static void init(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    public static void get(AbstractRequest request) {
        
    }

    public static void post(AbstractRequest request) {
        
    }

    static int checkDuration(String name, long duration, TimeUnit unit) {
        if (duration < 0) throw new IllegalArgumentException(name + " < 0");
        if (unit == null) throw new NullPointerException("unit == null");
        long millis = unit.toMillis(duration);
        if (millis > Integer.MAX_VALUE) throw new IllegalArgumentException(name + " too large.");
        if (millis == 0 && duration > 0) throw new IllegalArgumentException(name + " too small.");
        return (int) millis;
    }
}
