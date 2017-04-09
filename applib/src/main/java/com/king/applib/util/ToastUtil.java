package com.king.applib.util;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Toast工具类
 *
 * @author VanceKing
 * @since 2017/3/20.
 */

public class ToastUtil {
    public static void showShort(@NonNull String toast) {
        Toast.makeText(ContextUtil.getAppContext(), toast, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String toast, @NonNull String defaultText) {
        if (toast != null && !toast.trim().isEmpty()) {
            showShort(toast);
        } else {
            showShort(defaultText);
        }
    }

    public static void showShort(String toast, @StringRes int defResId) {
        if (toast != null && !toast.trim().isEmpty()) {
            showShort(toast);
        } else {
            showShort(defResId);
        }
    }

    public static void showShort(@StringRes int resId) {
        Toast.makeText(ContextUtil.getAppContext(), resId, Toast.LENGTH_SHORT).show();
    }


    public static void showLong(@NonNull String toast) {
        Toast.makeText(ContextUtil.getAppContext(), toast, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String toast, @NonNull String defaultText) {
        if (toast != null && !toast.trim().isEmpty()) {
            showShort(toast);
        } else {
            showLong(defaultText);
        }
    }

    public static void showLong(String toast, @StringRes int defResId) {
        if (toast != null && !toast.trim().isEmpty()) {
            showLong(toast);
        } else {
            showLong(defResId);
        }
    }

    public static void showLong(@StringRes int resId) {
        Toast.makeText(ContextUtil.getAppContext(), resId, Toast.LENGTH_LONG).show();
    }
}
