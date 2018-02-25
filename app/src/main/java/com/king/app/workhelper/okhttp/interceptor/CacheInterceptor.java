package com.king.app.workhelper.okhttp.interceptor;

import android.util.Log;

import com.king.applib.util.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author VanceKing
 * @since 2017/4/6.
 */

public class CacheInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtil.isNetworkAvailable()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (NetworkUtil.isNetworkAvailable()) {
            Log.i("aaa", "有网络");
            int maxAge = 0;
            // 有网络时, 不缓存, 最大保存时长为0
            response.newBuilder()
//                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .addHeader("Cache-Control", "public, max-age=0")
                    .removeHeader("Pragma")
                    .build();
        } else {
            Log.i("aaa", "无网络");
            // 无网络时，设置超时为4周
            int maxAge = 60;//60 * 60 * 24 * 28;
            response.newBuilder()
                    .header("Cache-Control", "max-age=60")//public, only-if-cached
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
