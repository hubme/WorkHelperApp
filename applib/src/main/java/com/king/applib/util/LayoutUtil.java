package com.king.applib.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Layout工具类
 *
 * @author VanceKing
 * @since 2017/4/5.
 */

public class LayoutUtil {
    private LayoutUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static void setContentView(Activity activity, View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        activity.setContentView(view);
    }
}
