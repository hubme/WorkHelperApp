package com.king.app.workhelper.adapter.viewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.king.applib.util.ExtendUtil;

import java.util.List;

/**
 * Fragment ViewPager 适配器
 *
 * @author VanceKing
 * @since 2016/12/20 0020.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments;
    private List<String> mTitles;

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }
    
    public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return ExtendUtil.isListNullOrEmpty(mFragments) ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override public CharSequence getPageTitle(int position) {
        return mTitles != null && !mTitles.isEmpty() ? mTitles.get(position) : null;
    }
}
