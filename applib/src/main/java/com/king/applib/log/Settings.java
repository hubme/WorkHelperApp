package com.king.applib.log;

public final class Settings {

    private int methodCount = 2;
    private boolean showThreadInfo = true;
    private boolean isShowLog = true;
    private int methodOffset = 0;
    private LogAdapter logAdapter;

    public Settings hideThreadInfo() {
        showThreadInfo = false;
        return this;
    }

    public Settings methodCount(int methodCount) {
        if (methodCount < 0) {
            methodCount = 0;
        }
        this.methodCount = methodCount;
        return this;
    }

    public boolean isShowLog() {
        return isShowLog;
    }

    public Settings setShowLog(boolean isShowLog) {
        this.isShowLog = isShowLog;
        return this;
    }

    public Settings methodOffset(int offset) {
        this.methodOffset = offset;
        return this;
    }

    public Settings logAdapter(LogAdapter logAdapter) {
        this.logAdapter = logAdapter;
        return this;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    public int getMethodOffset() {
        return methodOffset;
    }

    public LogAdapter getLogAdapter() {
        if (logAdapter == null) {
            logAdapter = new AndroidLogAdapter();
        }
        return logAdapter;
    }

    public void reset() {
        methodCount = 2;
        methodOffset = 0;
        showThreadInfo = true;
        isShowLog = true;
    }
}
