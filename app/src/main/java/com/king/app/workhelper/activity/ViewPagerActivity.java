package com.king.app.workhelper.activity;

import android.content.Intent;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.fragment.FragmentOne;
import com.king.applib.log.Logger;

public class ViewPagerActivity extends AppBaseActivity {

    private FragmentOne mFragmentOne;

    @Override
    public int getContentLayout() {
        return R.layout.activity_view_pager;
    }

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        String bundleValue = intent.getStringExtra(GlobalConstant.BUNDLE_PARAMS_KEY.EXTRA_KEY);
        Logger.i(bundleValue);
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