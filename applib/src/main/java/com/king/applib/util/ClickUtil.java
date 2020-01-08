package com.king.applib.util;

import android.os.SystemClock;

/**
 * @author VanceKing
 * @since 2020/1/8.
 */
public class ClickUtil {
    private static long mLastClickTime = 0;
    private static final int SPACE_TIME = 500;

    private ClickUtil() {

    }

    public static boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        if (time - mLastClickTime <= SPACE_TIME) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }
}
