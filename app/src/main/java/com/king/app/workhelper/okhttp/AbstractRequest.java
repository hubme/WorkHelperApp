package com.king.app.workhelper.okhttp;

import android.support.annotation.IntDef;

import java.util.concurrent.TimeUnit;

/**
 * 基础请求
 *
 * @author huoguangxu
 * @since 2017/4/7.
 */

public abstract class AbstractRequest<T> {
    public static final int GET = 1;
    public static final int POST = 2;
    public static final int PUT = 3;
    public static final int DELETE = 4;
    
    protected long connectTimeOut;

    @IntDef({GET, POST, PUT, DELETE})
    public @interface RequestMethod {

    }

    public void setConnectTimeout(long duration, TimeUnit unit) {
        connectTimeOut = OkHttpUtil.checkDuration("timeout", duration, unit);
    }
    
    
}
