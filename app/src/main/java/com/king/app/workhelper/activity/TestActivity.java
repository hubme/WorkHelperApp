package com.king.app.workhelper.activity;

import android.os.Looper;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override protected void initData() {

    }

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
    }

    @OnClick(R.id.tv_open_qq)
    public void onTestViewClick(CheckedTextView textView) {
        new Thread() {
            @Override public void run() {
                super.run();
                Looper.prepare();
                Toast.makeText(TestActivity.this, "哈哈哈", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
    }


}
