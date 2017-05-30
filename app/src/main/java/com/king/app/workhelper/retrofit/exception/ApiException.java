package com.king.app.workhelper.retrofit.exception;

/**
 * 公共的请求错误处理类
 *
 * @author VanceKing
 * @since 2017/5/27.
 */

public class ApiException extends RuntimeException {
    public static final int CODE_TIMEOUT = 1000;
    public static final String SOCKET_TIMEOUT_EXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";

    public static final int CODE_UNCONNECTED = 1001;
    public static final String CONNECT_EXCEPTION = "网络连接异常，请检查您的网络状态";

    public static final int CODE_MALFORMED_JSON = 1020;
    public static final String MALFORMED_JSON_EXCEPTION = "数据解析错误";

    public static final int CODE_DEFAULT = 1003;

    public int code;
    public String msg;

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
