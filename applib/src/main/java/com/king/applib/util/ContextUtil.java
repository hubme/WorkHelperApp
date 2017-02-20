package com.king.applib.util;

import android.content.Context;

/**
 * 上下文工具类
 *
 * @author huoguangxu
 * @since 2017/2/20.
 */

public class ContextUtil {
    private static Context mContext;

    private ContextUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static void init(Context context) {
        if (context != null) {
            mContext = context.getApplicationContext();
        }
    }

    public static Context getApplicationContext() {
        if (mContext != null) {
            return mContext;
        } else {
            throw new NullPointerException("u should init first");
        }
    }
}
