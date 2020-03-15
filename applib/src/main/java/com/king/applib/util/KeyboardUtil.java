package com.king.applib.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * 键盘显示/隐藏工具类
 * created by VanceKing at 2016/11/24
 */
public class KeyboardUtil {

    private final static int KEYBOARD_VISIBLE_THRESHOLD_DP = 100;

    /**
     * 设置键盘显示/隐藏监听事件
     *
     * @param activity 监听事件的Activity
     * @param listener 事件监听器
     */
    public static void setEventListener(final Activity activity, final OnKeyboardVisibilityEventListener listener) {
        if (activity == null || listener == null) {
            return;
        }
        final View rootActivity = getActivityRoot(activity);
        rootActivity.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    private final Rect rect = new Rect();
                    private final int visibleThreshold = Math.round(ExtendUtil.dp2px(KEYBOARD_VISIBLE_THRESHOLD_DP));

                    private boolean wasOpened = false;

                    @Override
                    public void onGlobalLayout() {
                        rootActivity.getWindowVisibleDisplayFrame(rect);
                        int heightDiff = rootActivity.getRootView().getHeight() - rect.height();
                        boolean isOpen = heightDiff > visibleThreshold;
                        if (isOpen != wasOpened) {
                            wasOpened = isOpen;
                            listener.onVisibilityChanged(isOpen);
                        }
                    }
                });
    }

    /**
     * 键盘是否显示/隐藏
     *
     * @param activity Activity
     * @return 键盘是否显示
     */
    public static boolean isKeyboardVisible(Activity activity) {
        Rect r = new Rect();

        View activityRoot = getActivityRoot(activity);
        int visibleThreshold = Math.round(ExtendUtil.dp2px( KEYBOARD_VISIBLE_THRESHOLD_DP));

        activityRoot.getWindowVisibleDisplayFrame(r);

        int heightDiff = activityRoot.getRootView().getHeight() - r.height();

        return heightDiff > visibleThreshold;
    }

    private static View getActivityRoot(Activity activity) {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    public interface OnKeyboardVisibilityEventListener {
        void onVisibilityChanged(boolean isOpen);
    }
}
