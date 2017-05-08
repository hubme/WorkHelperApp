package com.king.app.workhelper.okhttp.interceptor;

import com.king.applib.util.AppUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加默认请求头
 *
 * @author huoguangxu
 * @since 2017/5/8.
 */

public class HeadersInterceptor implements Interceptor {
    private final String mAppVersion;
    private static final String SOURCE = "10000";

    public HeadersInterceptor() {
        mAppVersion = AppUtil.getAppInfo().getVersionName();
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request = originalRequest.newBuilder().header("releaseVersion", mAppVersion)
                .header("source", SOURCE)
                .build();
        return chain.proceed(request);
    }
}
