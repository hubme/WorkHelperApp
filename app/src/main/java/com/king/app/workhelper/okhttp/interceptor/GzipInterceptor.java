package com.king.app.workhelper.okhttp.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * Gzip压缩拦截器.
 * <br/>
 * OkHttp3默认gzip压缩. see: {@link okhttp3.internal.http.BridgeInterceptor BridgeInterceptor}
 *
 * @author VanceKing
 * @since 2017/5/5.
 */

public class GzipInterceptor implements Interceptor {

    @Override public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest);
        }
        Request compressedRequest = originalRequest.newBuilder().header("Content-Encoding", "gzip").
                method(originalRequest.method(), gzip(originalRequest.body())).build();
        return chain.proceed(compressedRequest);
    }

    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // 无法知道压缩后的数据大小
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}
