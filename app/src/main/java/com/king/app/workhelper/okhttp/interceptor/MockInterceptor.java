package com.king.app.workhelper.okhttp.interceptor;

import com.king.app.workhelper.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Mock 数据的Interceptor.
 * 1.通过Interceptor拦截器模拟数据
 * 2.通过转接服务器模拟网络数据。eg: http://www.mocky.io/
 *
 * @author VanceKing
 * @since 2017/3/14.
 */

public class MockInterceptor implements Interceptor {
    private boolean isMock = false;
    @Override
    public Response intercept(Chain chain) throws IOException {
        String url = chain.request().url().toString();
        Response response = chain.proceed(chain.request());
        if (isMock && BuildConfig.LOG_DEBUG && url != null && url.contains("baidu")) {
            final String mockString = "{\"name\":\"VanceKing\",\"age\":\"28\"}";

            return new Response.Builder().code(200)
                    .message(mockString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(response.body().contentType(), mockString))
                    .addHeader("content-type", "application/json").build();
        } else {
            return response;
        }
    }
}
