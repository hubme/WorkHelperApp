package com.king.applib.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

/**
 * View 相关工具类.
 *
 * @author VanceKing
 * @since 2018/1/17.
 */
public class ViewUtil {
    private ViewUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /** 如果 TextView 为 null 或 TextView 的内容为“”/null,则返回 defaultText. */
    public static String getText(TextView textView, String defaultText) {
        if (textView == null) {
            return defaultText;
        }
        String string = textView.getText().toString();
        if (string.trim().isEmpty()) {
            return defaultText;
        }
        return string;
    }

    /** 如果 TextView 为 null,或 TextView 的内容为“”/null,则返回空字符串 "". */
    public static String getText(TextView textView) {
        return getText(textView, "");
    }

    /** View 设置背景的兼容方法 */
    public static void setViewBackground(View view, Drawable drawable) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
