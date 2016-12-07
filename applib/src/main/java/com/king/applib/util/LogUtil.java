package com.king.applib.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志
 * tag默认取类名，内容中带入了完整类路径和调用的方法名.
 * 格式化相关代码修改自
 * https://github.com/Robin-jiangyufeng/LazyLogger/blob/master/LoggerLibrary/src/main/java/com/robin/lazy/logger/LoggerFormattedPrinter.java
 * @author CJL
 * @since 2015-12-15
 */
public class LogUtil {
    // CHECKSTYLE:OFF
    private boolean debug;
    private String tag;

    private static final Object LOCK = new Object();

    final String LOG_FILE_NAME = "jz.log";

    public LogUtil() {
        this(null);
    }

    public LogUtil(String tag) {
        this(true, tag);
    }

    public LogUtil(boolean debug, String tag) {
        this.debug = debug;
        this.tag = tag;
    }

    public boolean debug() {
        return debug;
    }

    public void v(String msg) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.VERBOSE, m[0], m[1], null);
        }
    }

    public void v(String msg, Object... args) {
        if (debug) {
            String[] m = buildMessage(String.format(msg, args));
            log(Log.VERBOSE, m[0], m[1], null);
        }
    }

    public void v(String msg, Throwable thr) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.VERBOSE, m[0], m[1], thr);
        }
    }

    public void d(String msg) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.DEBUG, m[0], m[1], null);
        }
    }

    public void d(String msg, Object... args) {
        if (debug) {
            String[] m = buildMessage(String.format(msg, args));
            log(Log.DEBUG, m[0], m[1], null);
        }
    }

    public void d(String msg, Throwable thr) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.DEBUG, m[0], m[1], thr);
        }
    }

    public void i(String msg) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.INFO, m[0], m[1], null);
        }
    }

    public void i(String msg, Object... args) {
        if (debug) {
            String[] m = buildMessage(String.format(msg, args));
            log(Log.INFO, m[0], m[1], null);
        }
    }

    public void i(String msg, Throwable thr) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.INFO, m[0], m[1], thr);
        }
    }

    public void e(String msg) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.ERROR, m[0], m[1], null);
        }
    }

    public void e(String msg, Object... args) {
        if (debug) {
            String[] m = buildMessage(String.format(msg, args));
            log(Log.ERROR, m[0], m[1], null);
        }
    }

    public void e(String msg, Throwable thr) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.ERROR, m[0], m[1], thr);
        }
    }

    public void w(String msg) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.WARN, m[0], m[1], null);
        }
    }

    public void w(String msg, Object... args) {
        if (debug) {
            String[] m = buildMessage(String.format(msg, args));
            log(Log.WARN, m[0], m[1], null);
        }
    }

    public void w(String msg, Throwable thr) {
        if (debug) {
            String[] m = buildMessage(msg);
            log(Log.WARN, m[0], m[1], thr);
        }
    }

    /*public void file(String msg, Throwable thr) {
        if (!debug) {
            return;
        }
        File f = new File(JZApp.getAppContext().getExternalFilesDir(null), LOG_FILE_NAME);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f, true);
            String fm = new Date().toString() + "\n";
            fos.write(fm.getBytes(Charset.forName("utf-8")));
            fos.write(msg.getBytes(Charset.forName("utf-8")));
            thr.printStackTrace(new PrintStream(fos));
            fos.write("\n\n".getBytes());

        } catch (Exception e) {
            e("write file log failed");
            i(msg, thr);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // null
                }
            }
        }
    }*/

    /*public void file(String msg, Object... args) {
        if (!debug) {
            return;
        }
        File f = new File(JZApp.getAppContext().getExternalFilesDir(null), LOG_FILE_NAME);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f, true);
            String fm = new Date().toString() + "\n";
            fos.write(fm.getBytes(Charset.forName("utf-8")));
            fos.write(String.format(msg, args).getBytes(Charset.forName("utf-8")));
        } catch (Exception e) {
            e("write file log failed");
            i(msg, args);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // null
                }
            }
        }
    }*/

    protected String[] buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        String msgTag = tag;
        if (TextUtils.isEmpty(tag)) {
            msgTag = caller.getClassName().substring(caller.getClassName().lastIndexOf('.') + 1);
        }
        String res = caller.getClassName() + "#" + caller.getMethodName() + "():" + caller.getLineNumber() + "\n" + msg;
        return new String[]{msgTag, res};
    }

    /**
     * Android's max limit for a log entry is ~4076 bytes, so 4000 bytes is used
     * as chunk size since default charset is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;
    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    private void log(int logType, String tag, String message, Throwable thr) {
        synchronized (LOCK) {
            logTopBorder(logType, tag);
            logHeaderContent(logType, tag);

            byte[] bytes = message.getBytes();
            int length = bytes.length;
            if (length <= CHUNK_SIZE) {
                logContent(logType, tag, message);
            } else {
                for (int i = 0; i < length; i += CHUNK_SIZE) {
                    int count = Math.min(length - i, CHUNK_SIZE);
                    logContent(logType, tag, new String(bytes, i, count));
                }
            }

            // 打印Throwable信息
            if (thr != null) {
                logDivider(logType, tag);

                StringWriter sw = new StringWriter();
                thr.printStackTrace(new PrintWriter(sw));
                message = sw.toString();

                bytes = message.getBytes();
                length = bytes.length;
                if (length <= CHUNK_SIZE) {
                    logContent(logType, tag, message);
                } else {
                    for (int i = 0; i < length; i += CHUNK_SIZE) {
                        int count = Math.min(length - i, CHUNK_SIZE);
                        logContent(logType, tag, new String(bytes, i, count));
                    }
                }
            }

            logBottomBorder(logType, tag);
        }
    }

    private void logTopBorder(int logType, String tag) {
        logChunk(logType, tag, TOP_BORDER);
    }

    private void logHeaderContent(int logType, String tag) {
        logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " Thread: " + Thread.currentThread().getName());
        logDivider(logType, tag);
    }

    private void logBottomBorder(int logType, String tag) {
        logChunk(logType, tag, BOTTOM_BORDER);
    }

    private void logDivider(int logType, String tag) {
        logChunk(logType, tag, MIDDLE_BORDER);
    }

    private void logContent(int logType, String tag, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines) {
            logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " " + line);
        }
    }

    private void logChunk(int logType, String tag, String chunk) {
        switch (logType) {
            case Log.VERBOSE:
                Log.v(tag, chunk);
                break;
            case Log.ERROR:
                Log.e(tag, chunk);
                break;
            case Log.INFO:
                Log.i(tag, chunk);
                break;
            case Log.WARN:
                Log.w(tag, chunk);
                break;
            case Log.DEBUG:
                // Fall through, log debug by default
            default:
                Log.d(tag, chunk);
                break;
        }
    }

    // CHECKSTYLE:ON
}
