package com.king.app.workhelper.activity;

import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

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
        
    }
    
    @OnClick(R.id.tv_open_email)
    public void openEmail(TextView textView) {

        textView.animate().alpha(0.2f).setDuration(800).start();
    }
}
