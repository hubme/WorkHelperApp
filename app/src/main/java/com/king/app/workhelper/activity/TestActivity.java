package com.king.app.workhelper.activity;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.CheckedTextView;
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
    private Handler handler;
    private HandlerThread handlerThread;

    @Override protected void initData() {
        super.initData();
        handlerThread = new HandlerThread("MyHandlerThread");
//        handlerThread.start();

        /*handler = new Handler(handlerThread.getLooper()) {
            @Override public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        Log.i("aaa", "开始执行任务-0");
                        SystemClock.sleep(5000);
                        Log.i("aaa", "任务-0执行完毕");
                        break;
                    case 1:
                        Log.i("aaa", "开始执行任务-1");
                        SystemClock.sleep(2000);
                        Log.i("aaa", "任务-1执行完毕");
                        break;
                }
            }
        };*/
    }

    @BindView(R.id.tv_open_qq) TextView mTestTv;

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
    }

    @OnClick(R.id.tv_open_qq)
    public void onTestViewClick(CheckedTextView textView) {
        textView.toggle();
        if (textView.isChecked()) {
//            handler.sendEmptyMessage(0);
            Log.i("aaa", handlerThread.getLooper() == null ? "== null" : " != null");
            Log.i("aaa", "哈哈哈");
        } else {
//            handler.sendEmptyMessage(1);
            handlerThread.start();
        }
    }
}
