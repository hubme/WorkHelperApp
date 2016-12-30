package com.king.app.workhelper.okhttp;

import com.king.app.workhelper.app.AppConfig;
import com.king.applib.log.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * OkHttp3日志拦截器。添加后SimpleDraweeView显示不出图片。
 * Created by VanceKing on 2016/12/28 0028.
 */

public class LoggingInterceptor implements Interceptor {
    private static final String F_BREAK = " %n";
    private static final String F_URL = " %s";
    private static final String F_TIME = " in %.1fms";
    private static final String F_HEADERS = "%s";
    private static final String F_RESPONSE = F_BREAK + "Response: %d";
    private static final String F_BODY = "body: %s";

    private static final String F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK;
    private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
    private static final String F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER;
    private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
    private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;

    private boolean mIsPrintRequestHeaders = false;
    private boolean mIsPrintResponseHeaders = false;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = null;
        try {
            request = chain.request();

            long t1 = System.nanoTime();
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            MediaType contentType = null;
            String bodyString = "";
            String mediaType = "";
            if (response.body() != null) {
                contentType = response.body().contentType();
                bodyString = response.body().string();
                mediaType = contentType.type();
            }
            double time = (t2 - t1) / 1e6d;

            printLog(request, response, time, mediaType, bodyString);

            if (response.body() != null) {
                ResponseBody body = ResponseBody.create(contentType, bodyString);
                return response.newBuilder().body(body).build();
            } else {
                return response;
            }
        } catch (SocketTimeoutException e) {
            if (request != null) {
                Logger.log(Logger.INFO, AppConfig.LOG_TAG, "url: " + request.url() + "超时了.");
            } else {
                Logger.log(Logger.INFO, AppConfig.LOG_TAG, "request == null");
            }

            return chain.proceed(chain.request());
        }
    }

    private void printLog(Request request, Response response, double time, String mediaType, String bodyString) {
        switch (request.method()) {
            case "GET":
                Logger.log(Logger.INFO, AppConfig.LOG_TAG, String.format("GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY, request.url(), time, stringifyRequestHeaders(request), response.code(), stringifyResponseHeaders(response), stringifyResponseBody(mediaType, bodyString)));//response.headers()
                break;
            case "POST":
                Logger.log(Logger.INFO, AppConfig.LOG_TAG, String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, stringifyRequestHeaders(request), stringifyRequestBody(request), response.code(), stringifyResponseHeaders(response), stringifyResponseBody(mediaType, bodyString)));
                break;
            case "PUT":
                Logger.log(Logger.INFO, AppConfig.LOG_TAG, String.format("PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, stringifyRequestHeaders(request), request.body().toString(), response.code(), stringifyResponseHeaders(response), stringifyResponseBody(mediaType, bodyString)));
                break;
            case "DELETE":
                Logger.log(Logger.INFO, AppConfig.LOG_TAG, String.format("DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY, request.url(), time, stringifyRequestHeaders(request), response.code(), stringifyResponseHeaders(response)));
                break;
            default:
                break;
        }
    }

    private String stringifyRequestHeaders(Request request) {
        return mIsPrintRequestHeaders ? request.headers().toString() : "";
    }

    private String stringifyResponseHeaders(Response response) {
        return mIsPrintResponseHeaders ? response.headers().toString() : "";
    }

    private String stringifyRequestBody(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "IOException";
        }
    }

    private String stringifyResponseBody(String MediaType, String responseBody) {
        if (!"text".equalsIgnoreCase(MediaType)) {
            return "MIME类型是：" + MediaType + ",不打印log.";
        }
        return responseBody;
    }
}
