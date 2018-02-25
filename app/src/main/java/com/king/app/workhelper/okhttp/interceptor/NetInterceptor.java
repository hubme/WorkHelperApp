package com.king.app.workhelper.okhttp.interceptor;

import android.util.Log;

import com.king.app.workhelper.app.AppConfig;
import com.king.applib.util.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 有网络的情况下，先去读缓存。如果缓存过期，再从网络获取。
 *
 * @author VanceKing
 * @since 2017/4/26.
 */

public class NetInterceptor implements Interceptor {
    @Override 
    public Response intercept(Chain chain) throws IOException {
        Log.i(AppConfig.LOG_TAG, "NetInterceptor");
        Request request = chain.request();
        if (NetworkUtil.isNetworkAvailable()) {
            Log.i(AppConfig.LOG_TAG, "联网");
            Response response = chain.proceed(request);
            int maxAge = 10;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        }

        return chain.proceed(request);
    }
}
