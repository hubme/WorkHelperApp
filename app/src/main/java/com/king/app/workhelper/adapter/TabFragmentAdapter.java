package com.king.app.workhelper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.king.applib.util.ExtendUtil;

import java.util.List;

/**
 * Fragment ViewPager 适配器
 * Created by VanceKing on 2016/12/20 0020.
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments;

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return ExtendUtil.isListNullOrEmpty(mFragments) ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
