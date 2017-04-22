package com.king.app.workhelper.okhttp;

import android.support.annotation.IntDef;
import android.util.Log;

import com.king.app.workhelper.app.AppConfig;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * OkHttp 拦截器
 *
 * @author VanceKing
 * @since 2017/4/20.
 */

public class OkHttpLogInterceptor implements Interceptor {
    private static final String TAG = AppConfig.LOG_TAG;
    private static final int CHUNK_SIZE = 4000;

    private static final String ONE_SPACE = " ";
    private static final String NEW_LINE = "\n║";
    private static final String LINE_SINGLE_SEPARATE = "║----------------------------------------------------------------------------------------";
    private static final String LINE_START = "╔════════════════════════════════════════════════════════════════════════════════════════";
    private static final String LINE_END = "╚════════════════════════════════════════════════════════════════════════════════════════";
    private static final String LINE_SHORT = "║";

    public static final int NONE = 0;//不打印log
    public static final int BASAL = 1;//不包含headers
    public static final int ALL = 2;//包含headers
    @LogLevel
    private static int mLevel = BASAL;

    @IntDef({NONE, BASAL, ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LogLevel {
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long start = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (SocketTimeoutException e) {
            log(request.url().toString() + " 超时");
            return chain.proceed(chain.request());
        } catch (Exception e) {
            log(request.url().toString() + " 失败.可能断网、接口挂了");
            return chain.proceed(chain.request());
        }
        if (mLevel == NONE) {
            return response;
        }
        long duration = System.nanoTime() - start;
        String time = String.format(Locale.getDefault(), "%.1fms", duration / 1e6d);

        log(LINE_START);
        log(stringifyRequest(request, time));
        log(LINE_SINGLE_SEPARATE);
        logResponse(request.method(), response);
        log(LINE_END);

        return response;
    }

    private void logResponse(String method, Response response) {
        log(LINE_SHORT + "response: " + response.code());

        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            long contentLength = responseBody.contentLength();
            String len = contentLength != -1 ? contentLength + " bytes" : "unknown-length";
            log(LINE_SHORT + "mime: " + responseBody.contentType().toString() + " length: " + len);
        }

        if (mLevel == ALL) {
            log(stringifyResponseHeaders(response));
        }

        logResponseBody(method, responseBody);
    }

    private String stringifyRequest(Request request, String duration) {
        if (request == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_SHORT).append(request.method()).append(ONE_SPACE).append(request.url()).append(ONE_SPACE).append(duration).append(NEW_LINE);
        if (mLevel == ALL) {
            Headers headers = request.headers();
            for (int i = 0, size = headers.size(); i < size; i++) {
                sb.append(headers.name(i)).append(": ").append(headers.value(i)).append(NEW_LINE);
            }
        }
        sb.append(stringifyRequestBody(request));

        return sb.toString();
    }

    private String stringifyRequestBody(Request request) {
        if (request == null) {
            return "";
        }
        final String method = request.method();
        if ("GET".equalsIgnoreCase(method) || "DELETE".equals(method)) {
            return "no request body";
        }
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            return "stringify request occur Exception";
        }
    }

    private String stringifyResponseHeaders(Response response) {
        StringBuilder sb = new StringBuilder();
        sb.append(NEW_LINE).append(NEW_LINE);
        Headers headers = response.headers();
        for (int i = 0, size = headers.size(); i < size; i++) {
            sb.append(headers.name(i)).append(": ").append(headers.value(i)).append(NEW_LINE);
        }
        return sb.toString();
    }

    private void logResponseBody(String method, ResponseBody responseBody) {
        if (responseBody == null || "DELETE".equalsIgnoreCase(method)) {
            return;
        }
        if (!isPlaintext(responseBody.contentType())) {
            log("不是文本，不打印log");
        }
        try {
            // 不要使用responseBody.string(),因为只能使用一次。当执行回调是会出错：IllegalStateException: closed.
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            String results = buffer.clone().readString(Charset.defaultCharset());
            int size = results.length();
            if (size <= CHUNK_SIZE) {
                log(LINE_SHORT + results);
            } else {//超过log最大长度，分段输入
                final byte[] bytes = results.getBytes();
                for (int i = 0; i < size; i += CHUNK_SIZE) {
                    int count = Math.min(size - i, CHUNK_SIZE);
                    log(LINE_SHORT + new String(bytes, i, count));
                }
            }
        } catch (SocketTimeoutException e) {
            log("超时");
        } catch (IOException e) {
            log("response is null.");
        } catch (Exception e) {
            log("失败.可能断网、接口挂了" + e.toString());
        }
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            if (subtype.contains("x-www-form-urlencoded") ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html"))
                return true;
        }
        return false;
    }

    public static void setLogLevel(@LogLevel int level) {
        mLevel = level;
    }

    private void log(String content) {
        Log.i(TAG, content);
    }
}
