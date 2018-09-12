package com.king.app.workhelper.activity;

import android.os.SystemClock;
import android.util.Log;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.util.ThreadPoolUtil;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @Override protected void initInitialData() {
        super.initInitialData();
    }

    @Override protected void initData() {

    }

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();


    }

    @OnClick(R.id.textView)
    public void onViewClick() {
        ThreadPoolUtil.execute(new Runnable() {
            @Override public void run() {
                Log.i("aaa", "job begin!");
                SystemClock.sleep(5000);
                Log.i("aaa", "job done!");
            }
        });
    }

}
