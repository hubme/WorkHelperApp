package com.king.app.workhelper.adapter.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by HuoGuangxu on 2016/9/30.
 */

public class TestPagerAdapter extends PagerAdapter {
    public TestPagerAdapter() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
