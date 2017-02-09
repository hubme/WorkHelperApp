package com.king.app.workhelper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.TabFragmentAdapter;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.FirstTabFragment;
import com.king.app.workhelper.fragment.SecondTabFragment;
import com.king.app.workhelper.fragment.ThirdTabFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Fragment+ViewPager+Tab
 * Created by VanceKing on 2016/12/20 0020.
 */

public class TabSwitchActivity extends AppBaseActivity {
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    @Override
    public int getContentLayout() {
        return R.layout.activity_tab_switch;
    }

    @Override
    protected String getActivityTitle() {
        return "Tab切换";
    }

    @Override
    protected void initData() {
        super.initData();

        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new FirstTabFragment());
        mFragments.add(new SecondTabFragment());
        mFragments.add(new ThirdTabFragment());

        TabFragmentAdapter mTabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mTabFragmentAdapter);
        mViewPager.setOffscreenPageLimit(1);//缓存当前页面前后各1个，之外的将销毁。
    }
}
