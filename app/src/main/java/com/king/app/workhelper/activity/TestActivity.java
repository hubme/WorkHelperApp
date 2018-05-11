package com.king.app.workhelper.activity;

import android.content.Intent;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.service.IntentServiceSample;

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
        Intent intent = new Intent(this, IntentServiceSample.class);
        intent.putExtra("name", "aaa");
        startService(intent);

        Intent intent2 = new Intent(this, IntentServiceSample.class);
        intent2.putExtra("name", "bbb");
        startService(intent2);

        startService(intent);
    }
}
