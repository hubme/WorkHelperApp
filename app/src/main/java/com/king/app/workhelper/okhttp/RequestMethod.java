package com.king.app.workhelper.okhttp;

import android.support.annotation.IntDef;

/**
 * Http请求方式.
 *
 * @author VanceKing
 * @since 2017/4/10.
 */

public class RequestMethod {
    public static final int GET = 1;
    public static final int POST = 2;
    public static final int PUT = 3;
    public static final int DELETE = 4;

    @IntDef({GET, POST, PUT, DELETE})
    public @interface Method {

    }
}
