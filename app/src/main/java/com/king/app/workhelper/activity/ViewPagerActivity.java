package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.fragment.FragmentOne;

public class ViewPagerActivity extends AppBaseActivity {

    private FragmentOne mFragmentOne;

    @Override
    public int getContentLayout() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        if (mFragmentOne == null) {
            mFragmentOne = new FragmentOne();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.my_frame, mFragmentOne).commit();
    }
}