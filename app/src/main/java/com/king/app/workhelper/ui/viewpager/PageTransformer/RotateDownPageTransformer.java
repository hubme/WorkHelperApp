package com.king.app.workhelper.ui.viewpager.PageTransformer;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * @author VanceKing
 * @since 2017/12/10.
 */

public class RotateDownPageTransformer implements ViewPager.PageTransformer {

    private static final float ROT_MAX = 20.0f;
    private float mRot;

    public void transformPage(View view, float position) {
        if (Math.abs(position) > 1) {
            ViewHelper.setRotation(view, 0);

        } else {// a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0
            // [-1,1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0) {
                mRot = (ROT_MAX * position);
                ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(view, view.getMeasuredHeight());
                ViewHelper.setRotation(view, mRot);
            } else {
                mRot = (ROT_MAX * position);
                ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(view, view.getMeasuredHeight());
                ViewHelper.setRotation(view, mRot);
            }
        }
    }
}
