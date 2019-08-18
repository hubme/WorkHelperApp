package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.common.RxBus;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {

    @Override
    protected void initData() {
        
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @OnClick(R.id.tv_permission)
    public void onPermissionClick() {
        RxBus.getInstance().post("1024", "哈哈哈");
    }

}
