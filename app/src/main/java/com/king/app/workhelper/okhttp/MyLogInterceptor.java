package com.king.app.workhelper.okhttp;

import android.support.annotation.IntDef;
import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @author huoguangxu
 * @since 2017/4/20.
 */

public class MyLogInterceptor implements Interceptor {
    private static final String TAG = "bbb";
    private static final String ONE_SPACE = " ";
    private static final String NEW_LINE = "\n";
    private static final String LINE_SINGLE_SEPARATE = "-----------------------------------------------------------------------------------------";
    private static final String LINE_DOUBLE_BEGIN =    "=========================================================================================";
    
    
    public static final int NONE = 0;//不打印log
    public static final int BASAL = 1;//不打印headers
    public static final int REQUEST_HEADER = 2;//打印请求headers
    public static final int RESPONSE_HEADER = 3;//打印相应headers
    public static final int ALL = 4;//全部打印
    @LogLevel private int mLevel = ALL;

    @IntDef({NONE, BASAL, REQUEST_HEADER, RESPONSE_HEADER, ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LogLevel {
    }


    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long start = System.nanoTime();
        Response response = chain.proceed(request);
        long duration = System.nanoTime() - start;
        if (mLevel == NONE) {
            return response;
        }
        ResponseBody responseBody = response.body();
        String time = String.format(Locale.getDefault(), "%.1fms", duration / 1e6d);
//        Log.i(TAG, buildRequestText(request, time) + LINE_SINGLE_SEPARATE + NEW_LINE + stringifyResponseBody(response.newBuilder().body(responseBody).build()));
        Log.i(TAG, String.format(Locale.getDefault(), "%s\n%s%s\n%s%s", LINE_DOUBLE_BEGIN, buildRequestText(request, time), 
                LINE_SINGLE_SEPARATE, stringifyResponseBody(response.newBuilder().body(responseBody).build()), LINE_DOUBLE_BEGIN));
        return chain.proceed(request);
    }

    private String buildRequestText(Request request, String duration) {
        if (request == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(request.method()).append(ONE_SPACE).append(request.url()).append(ONE_SPACE).append(duration).append(NEW_LINE);
        if (mLevel == REQUEST_HEADER || mLevel == ALL) {
            Headers headers = request.headers();
            for (int i = 0, size = headers.size(); i < size; i++) {
                sb.append(headers.name(i)).append(": ").append(headers.value(i)).append(NEW_LINE);
            }
        }
        sb.append(stringifyRequestBody(request)).append(NEW_LINE);

        return sb.toString();
    }

    private String stringifyRequestBody(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            return "stringify request occur Exception";
        }
    }

    private String stringifyResponseBody(Response response) {
        if (response == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("response: ").append(response.code()).append(NEW_LINE);
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            sb.append("mime: ").append(responseBody.contentType().toString()).append(" length: ");
            long contentLength = responseBody.contentLength();
            sb.append(contentLength != -1 ? contentLength + " bytes" : "unknown-length").append(NEW_LINE);
        }

        //response body
        if (responseBody != null) {
            if (isPlaintext(responseBody.contentType())) {
                try {
                    sb.append(responseBody.string());
                } catch (IOException e) {
                    sb.append("response is null.");
                }
            } else {
                sb.append("不是文本，不打印log");
            }

            sb.append(NEW_LINE);
        }


        //headers
        if (mLevel == RESPONSE_HEADER || mLevel == ALL) {
            sb.append(NEW_LINE);
            Headers headers = response.headers();
            for (int i = 0, size = headers.size(); i < size; i++) {
                sb.append(headers.name(i)).append(": ").append(headers.value(i)).append(NEW_LINE);
            }
        }
        return sb.toString();
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) return false;
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

    private boolean isImageType(MediaType mediaType) {
        return mediaType != null && mediaType.type() != null && mediaType.type().equals("image");
    }

    public void setLogLevel(@LogLevel int level) {
        this.mLevel = level;
    }
}
