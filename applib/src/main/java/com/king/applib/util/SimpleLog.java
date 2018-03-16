package com.king.applib.util;

import android.util.Log;

/**
 * 日志工具类。
 *
 * @author VanceKing
 * @since 2016/12/6
 */
public class SimpleLog {
    private static final String TAG_DEFAULT = "SimpleLog";
    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";

    private static final SimpleLog mLog = new SimpleLog();
    private boolean mDebug = false;
    private String mTag = TAG_DEFAULT;

    private SimpleLog() {
    }

    public static void i(String msg) {
        Log.i(TAG_DEFAULT, buildLogMsg(msg));
    }

    private static String buildLogMsg(String msg) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length < 3) {
            return "";
        }
        StackTraceElement trace = stackTrace[2];
        StringBuilder sb = new StringBuilder();
        sb.append("(")
                .append(trace.getFileName())
                .append(":")
                .append(stackTrace[3].getLineNumber())
                .append(")")
                .append("\n")
                .append(msg);
        return sb.toString();
    }
}
