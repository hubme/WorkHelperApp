package com.king.app.workhelper.activity;

import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @BindView(R.id.tv_open_qq) TextView mTestTv;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
    }

    @OnClick(R.id.tv_open_qq)
    public void onTestViewClick(TextView textView) {
        new Thread(){
            @Override public void run() {
                super.run();
                mTestTv.setText("喔喔喔");
            }
        }.start();
    }
}
