package com.king.applib.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用 FragmentPagerAdapter。
 *
 * @author VanceKing
 * @since 2017/11/17.
 */

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();

    public CommonFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public CommonFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        setFragments(fragments);
    }

    @Override public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override public int getCount() {
        return mFragments.size();
    }

    public void setFragments(List<Fragment> fragments) {
        if (isFragmentsEmpty(fragments)) {
            return;
        }
        mFragments.addAll(fragments);
        notifyDataSetChanged();
    }

    private boolean isFragmentsEmpty(List<Fragment> fragments) {
        return fragments == null || fragments.isEmpty();
    }
}
