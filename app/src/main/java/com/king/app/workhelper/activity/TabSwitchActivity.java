package com.king.app.workhelper.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.viewpager.TabFragmentAdapter;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.FirstTabFragment;
import com.king.app.workhelper.fragment.SecondTabFragment;
import com.king.app.workhelper.fragment.SmartRefreshSampleFragment;
import com.king.app.workhelper.fragment.ThirdTabFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Fragment+ViewPager+Tab
 * Created by VanceKing on 2016/12/20.
 */

public class TabSwitchActivity extends AppBaseActivity {
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;

    @Override
    public int getContentLayout() {
        return R.layout.activity_tab_switch;
    }

    @Override
    protected String getActivityTitle() {
        return "Tab切换";
    }

    @Override protected void initContentView() {
        super.initContentView();
        //TabLayout 和 ViewPager 关联
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        super.initData();

        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new FirstTabFragment());
        mFragments.add(new SecondTabFragment());
        mFragments.add(new ThirdTabFragment());
        mFragments.add(new SmartRefreshSampleFragment());

        List<String> mTitles = new ArrayList<>(mFragments.size());
        mTitles.add("First");
        mTitles.add("Second");
        mTitles.add("Third");
        mTitles.add("SmartRefreshLayout");

        TabFragmentAdapter mTabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mTabFragmentAdapter);
        mViewPager.setOffscreenPageLimit(1);//缓存当前页面前后各1个，之外的将销毁

        mTabLayout.getTabAt(0).setText(mTitles.get(0));
        mTabLayout.getTabAt(1).setText(mTitles.get(1));
        mTabLayout.getTabAt(2).setText(mTitles.get(2)).select();
        mTabLayout.getTabAt(3).setText(mTitles.get(3));
        
        /*mTabLayout.addTab(mTabLayout.newTab().setText("First"), 0, false);
        mTabLayout.addTab(mTabLayout.newTab().setText("Second"), 1, false);
        mTabLayout.addTab(mTabLayout.newTab().setText("Third"), true);*/
    }
}
