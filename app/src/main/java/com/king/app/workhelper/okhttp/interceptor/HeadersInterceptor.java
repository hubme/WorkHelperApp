package com.king.app.workhelper.okhttp.interceptor;

import com.king.applib.util.AppUtil;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加默认请求头
 *
 * @author VanceKing
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
        final Request.Builder headerRequestBuilder = originalRequest.newBuilder();
        
        Request headerRequest = headerRequestBuilder.header("releaseVersion", mAppVersion)
                .header("source", SOURCE)
                .build();

        //Post参数
        if (headerRequest.body() instanceof FormBody) {
            final FormBody.Builder newFormBuilder = new FormBody.Builder();
            final FormBody oldFormBody = (FormBody) headerRequest.body();
            for (int i = 0; i < oldFormBody.size(); i++) {
                final String key = oldFormBody.encodedName(i);
                newFormBuilder.addEncoded(key, oldFormBody.encodedValue(i));
            }

            newFormBuilder.add("releaseVersion", mAppVersion)
                    .add("source", SOURCE);
            

            headerRequestBuilder.method(headerRequest.method(), newFormBuilder.build());
        }

        final Request newRequest = headerRequestBuilder.build();
        return chain.proceed(newRequest);
    }
}
