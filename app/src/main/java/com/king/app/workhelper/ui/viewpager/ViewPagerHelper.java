package com.king.app.workhelper.ui.viewpager;

import android.support.v4.view.ViewPager;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * ViewPager帮助类
 * Created by HuoGuangxu on 2016/10/12.
 */

public class ViewPagerHelper {
    private void ViewPageHelper() {

    }

    /**
     * 设置ViewPager切换时长,默认1s.<br/>
     * see: {@link Scroller#getDuration()}
     */
    public static void setDefaultPagerSwitchDuration(ViewPager viewPager) {
        setPagerSwitchDuration(viewPager, 1000);
    }

    /**
     * 设置ViewPager切换时长.
     *
     * @param duration 切换时长.see: {@link Scroller#getDuration()}
     */
    public static void setPagerSwitchDuration(ViewPager viewPager, int duration) {
        if (viewPager == null) {
            return;
        }
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);

            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
            scroller.setDuration(duration);
            mField.set(viewPager, scroller);
        } catch (Exception e) {
        }

    }
}
