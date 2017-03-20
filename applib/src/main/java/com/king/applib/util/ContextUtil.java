package com.king.applib.util;

import android.content.Context;

/**
 * 上下文工具类
 *
 * @author huoguangxu
 * @since 2017/2/20.
 */

public class ContextUtil {
    private static Context mAppContext;

    private ContextUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static void init(Context context) {
        if (context != null) {
            mAppContext = context.getApplicationContext();
        }
    }

    public static Context getAppContext() {
        if (mAppContext != null) {
            return mAppContext;
        } else {
            throw new NullPointerException("u should init in Application first");
        }
    }
}
