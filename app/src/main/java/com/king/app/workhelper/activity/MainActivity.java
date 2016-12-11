package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.OnClick;

public class MainActivity extends AppBaseActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick(R.id.tv_main)
    public void onMainClick() {
    }
}
