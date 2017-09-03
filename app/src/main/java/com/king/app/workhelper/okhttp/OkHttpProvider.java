package com.king.app.workhelper.okhttp;

import android.os.Environment;

import com.king.app.workhelper.BuildConfig;
import com.king.app.workhelper.okhttp.interceptor.LogInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * OkHttpClient提供者
 *
 * @author VanceKing
 * @since 2017/5/30.
 */

public class OkHttpProvider {
    public static final long DEFAULT_CONNECT_TIMEOUT = 10;
    public static final long DEFAULT_WRITE_TIMEOUT = 20;
    public static final long DEFAULT_READ_TIMEOUT = 10;

    private OkHttpClient mOkHttpClient;

    private OkHttpProvider() {
        initOkHttp();
    }

    private static class SingletonHolder {
        private static final OkHttpProvider INSTANCE = new OkHttpProvider();
    }

    public static OkHttpProvider getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(new Cache(new File(Environment.getExternalStorageDirectory().getPath()+"/000test/cache"), 5 * 1024 * 1024L))
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        if (BuildConfig.LOG_DEBUG) {
            builder.addInterceptor(new LogInterceptor());
        }
        mOkHttpClient = builder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}
