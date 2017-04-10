package com.king.app.workhelper.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 基础请求
 *
 * @author huoguangxu
 * @since 2017/4/7.
 */

public abstract class AbstractRequest {
    protected String url;
    protected Object tag;

    public AbstractRequest setConnectTimeout(long duration, TimeUnit unit) {
        SimpleOkHttp.getInstance().getOkHttpClient().newBuilder().connectTimeout(duration, unit);
        return this;
    }
    
    public AbstractRequest setReadTimeout(long duration, TimeUnit unit) {
        SimpleOkHttp.getInstance().getOkHttpClient().newBuilder().readTimeout(duration, unit);
        return this;
    }

    public AbstractRequest setWriteTimeout(long duration, TimeUnit unit) {
        SimpleOkHttp.getInstance().getOkHttpClient().newBuilder().writeTimeout(duration, unit);
        return this;
    }

    public AbstractRequest tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public AbstractRequest url(String url) {
        this.url = url;
        return this;
    }
    
    public Call build() {
        Request request = new Request.Builder().tag(tag).url(url).build();
        return SimpleOkHttp.getInstance().getOkHttpClient().newCall(request);
    }
    
    
}
