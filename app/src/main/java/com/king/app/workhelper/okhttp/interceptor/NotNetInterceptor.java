package com.king.app.workhelper.okhttp.interceptor;

import android.util.Log;

import com.king.app.workhelper.app.AppConfig;
import com.king.applib.util.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 没有网络时，读取缓存数据
 *
 * @author huoguangxu
 * @since 2017/4/26.
 */

public class NotNetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.i(AppConfig.LOG_TAG, "NotNetInterceptor");
        Request request = chain.request();
        if (!NetworkUtil.isNetworkAvailable()) {
            Log.i(AppConfig.LOG_TAG, "无网络");
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();

            Response response = chain.proceed(request);
            int maxStale = 3600 * 24;
            return response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        return chain.proceed(request);
    }
}
