package com.king.app.workhelper.okhttp;

import okhttp3.Call;
import okhttp3.Request;

/**
 * @author VanceKing
 * @since 2017/4/10.
 */

public class RequestCall {
    private AbstractRequest request;
    private Call call;

    public RequestCall(AbstractRequest request) {
        this.request = request;
    }

    private Call build() {
        Request request = new Request.Builder().build();
        return SimpleOkHttp.getInstance().getOkHttpClient().newCall(request);
    }
}
