package com.king.applib.simplebanner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

class ViewPagerScroller extends Scroller {
    public static final int DEFAULT_DURATION = 1000;
    public int mDuration = DEFAULT_DURATION;

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setDuration(int duration) {
        if (duration > 0) {
            mDuration = duration;
        }
    }

}
