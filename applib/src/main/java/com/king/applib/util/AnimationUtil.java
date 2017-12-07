package com.king.applib.util;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * 动画工具类。
 *
 * @author VanceKing
 * @since 2017/1/17.
 */

public class AnimationUtil {
    private AnimationUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /** 高度渐变动画 */
    public static void animHeightToView(final View view, final int start, final int end, long duration) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams.height = (int) animation.getAnimatedValue();
                view.setLayoutParams(layoutParams);
                view.requestLayout();
            }
        });

        animator.setDuration(duration);
        animator.start();
    }

}
