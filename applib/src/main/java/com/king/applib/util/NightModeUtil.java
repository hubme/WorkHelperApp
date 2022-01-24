package com.king.applib.util;

import android.app.UiModeManager;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * 黑色模式相关工具类。
 *
 * @author VanceKing
 * @since 2022/1/24
 */
public class NightModeUtil {

    private NightModeUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 判断当前系统是否是黑色模式。
     */
    public static boolean isNightMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

    /**
     * 初始化App深色模式
     *
     * @param systemMode 是否是跟随系统
     * @param nightMode  是否是深色模式
     */
    public static void initNightMode(boolean systemMode, boolean nightMode) {
        if (systemMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else {
            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }
}
