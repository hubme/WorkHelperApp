package com.king.app.workhelper.adapter.viewpager;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/5/12.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    private final List<View> mPagerViews = new ArrayList<>();

    public MyViewPagerAdapter() {
    }

    public MyViewPagerAdapter(List<View> pagerViews) {
        if (pagerViews == null || pagerViews.isEmpty()) {
            return;
        }
        mPagerViews.addAll(pagerViews);
    }

    public void setAdapterData(List<View> pagerViews) {
        setAdapterData(pagerViews, false);
    }

    public void setAdapterData(List<View> pagerViews, boolean isAppend) {
        if (pagerViews == null || pagerViews.isEmpty()) {
            return;
        }
        if (!isAppend && !mPagerViews.isEmpty()) {
            mPagerViews.clear();
        }
        mPagerViews.addAll(pagerViews);
        notifyDataSetChanged();
    }

    public void resetAdapterData() {
        mPagerViews.clear();
        notifyDataSetChanged();
    }

    private List<View> getAdapterData() {
        return mPagerViews;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;//解决notifyDataSetChanged无效的问题
    }

    @Override
    public int getCount() {
        return mPagerViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mPagerViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "标题";
    }
}
