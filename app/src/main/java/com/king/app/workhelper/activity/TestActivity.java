package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.util.ThirdOpenUtil;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();

    }

    @OnClick(R.id.tv_open_qq)
    public void openQQ() {
        //QQ或Tim能处理"mqqwpa://im/chat"协议
        ThirdOpenUtil.openQQTim(this, "563918176");
    }
    
    @OnClick(R.id.tv_open_email)
    public void openEmail() {

        ThirdOpenUtil.sendEmail(this, "huogxu@163.com");
    }
}
