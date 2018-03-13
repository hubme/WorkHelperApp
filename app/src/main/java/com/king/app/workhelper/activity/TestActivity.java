package com.king.app.workhelper.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
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
    private ProgressDialog mProgressDialog;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
        mProgressDialog = new ProgressDialog(this);
        HandlerThread handlerThread = new HandlerThread("atthread");
        handlerThread.start();
        Handler mHandler = new Handler(handlerThread.getLooper());
        mHandler.post(new AtThread());

    }

    public class AtThread implements Runnable {
        @Override
        public void run() {
            mProgressDialog.setMessage("哈哈哈");
            mProgressDialog.show();
            mProgressDialog.dismiss();
        }
    }

    @OnClick(R.id.tv_open_qq)
    public void onTestViewClick(TextView textView) {
        new Thread(){
            @Override public void run() {
                super.run();
                Looper.prepare();
                mProgressDialog.show();
            }
        }.start();
    }
}
