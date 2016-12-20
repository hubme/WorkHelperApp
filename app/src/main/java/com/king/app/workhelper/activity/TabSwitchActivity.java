package com.king.app.workhelper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.king.app.workhelper.R;
import com.king.app.workhelper.adapter.TabFragmentAdapter;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.FirstTabFragment;
import com.king.app.workhelper.fragment.SecondTabFragment;
import com.king.app.workhelper.fragment.ThirdTabFragment;
import com.king.app.workhelper.ui.customview.SwitchTitle;
import com.king.applib.log.Logger;
import com.king.applib.ui.customview.BadgeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Fragment+ViewPager+Tab
 * Created by VanceKing on 2016/12/20 0020.
 */

public class TabSwitchActivity extends AppBaseActivity implements SwitchTitle.PageChangeListener {
    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    @BindView(R.id.tab_switch)
    public SwitchTitle mSwitchTitle;

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

        mSwitchTitle.setParams(mViewPager, Arrays.asList("第一个", "第一个", "第二个"), this);

        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new FirstTabFragment());
        mFragments.add(new SecondTabFragment());
        mFragments.add(new ThirdTabFragment());

        TabFragmentAdapter mTabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mTabFragmentAdapter);

        SwitchTitle.TabContainer tabContainer = mSwitchTitle.getTabContainer();
        SwitchTitle.TabText tabText = (SwitchTitle.TabText) tabContainer.getChildAt(0);
        BadgeView badgeView = new BadgeView(this, tabText);
        badgeView.setText("10");
        badgeView.show();
    }

    @Override
    public void pageChange(int position) {
        Logger.i("position: " + position);
    }
}
