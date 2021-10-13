package com.king.applib.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.util.Pair;

/**
 * 屏幕相关工具类。
 *
 * @author VanceKing
 * @since 2016/10/27
 */
public class ScreenUtil {
    private ScreenUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 获取屏幕的宽度px
     *
     * @param context 上下文
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     *
     * @param context 上下文
     * @return 屏幕高px
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕宽高。
     * <br>
     * {@link Display#getRealMetrics(DisplayMetrics)} 不包含状态栏和导航栏的高度。华为荣耀手机包含状态栏。
     */
    public static Pair<Integer, Integer> getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(dm);
        return new Pair<>(dm.widthPixels, dm.heightPixels);
    }

    /**
     * 检查是否存在导航栏。
     */
    public static boolean checkHasNavigationBar(Context context) {
        final Resources resources = context.getResources();
        int naviId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (naviId != 0) {
            return resources.getBoolean(naviId);
        }
        return false;
    }

    /**
     * 获取导航栏的高度。导航栏隐藏后同样有值。测试发现，目前没有方法判断是否显示了导航栏。
     */
    public static int getNavigationBarHeight(Context context) {
        final Resources resources = context.getResources();
        if (checkHasNavigationBar(context)) {
            int id = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (id != 0) {
                return resources.getDimensionPixelSize(id);
            }
        }
        return 0;
    }

    /*public static void isNavigationBarExist(Activity activity) {
        final int height = getNavigationBarHeight(activity);

        View decorView = activity.getWindow().getDecorView();
        decorView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets windowInsets) {
                decorView.setOnApplyWindowInsetsListener(null);
                if (windowInsets != null) {
                    int bottomInset = windowInsets.getSystemWindowInsetBottom();
                    boolean result = (height == bottomInset);
                    Log.i("aaa", "aaa: " + (result ? "非全屏" : "全屏"));
                }

                return windowInsets;
            }
        });
    }*/

    /*public static boolean isNavigationBarVisible(@NonNull Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0, size = vp.getChildCount(); i < size; i++) {
                int id = vp.getChildAt(i).getId();
                if (id != View.NO_ID && "navigationBarBackground".equals(activity.getResources().getResourceEntryName(id))) {
                    return true;
                }
            }
        }
        return false;
    }*/

    /**
     * 获取状态栏的高度。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        final Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
