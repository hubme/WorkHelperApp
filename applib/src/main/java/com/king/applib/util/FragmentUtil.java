package com.king.applib.util;

import android.support.v4.app.FragmentManager;

/**
 * Fragment 相关工具类。
 *
 * @author VanceKing
 * @since 2018/1/22.
 */

public class FragmentUtil {
    private FragmentUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /** 判断 FragmentManager 是否可用。可用返回 true,否则返回 flase */
    public static boolean isFragmentManagerAvailable(FragmentManager fragmentManager) {
        return fragmentManager != null && !fragmentManager.isDestroyed();
    }
}
