package com.king.app.workhelper.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * @author huoguangxu
 * @since 2017/4/7.
 */

public class SimpleOkHttp {
    private static OkHttpClient mOkHttpClient;

    private SimpleOkHttp() {
        
    }

    private static class HttpClientHolder {
        private static SimpleOkHttp okHttp = new SimpleOkHttp();
    }

    public static SimpleOkHttp getInstance() {
        if (mOkHttpClient == null) {
            throw new RuntimeException("please init OkHttpClient first!");
        }
        return HttpClientHolder.okHttp;
    }

    public void init(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public GetRequestBuilder get() {
        return new GetRequestBuilder();
    }

    public void post() {

    }

    public void cancel(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void cancelAll() {
        mOkHttpClient.dispatcher().cancelAll();
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
