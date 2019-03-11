package com.king.app.workhelper.activity;

import android.os.SystemClock;
import android.util.Log;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.OnClick;
import hugo.weaving.DebugLog;

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


    @OnClick(R.id.tv_open)
    public void onViewClick() {
        printLog();
    }

    @DebugLog
    private void printLog() {
        SystemClock.sleep(3000);
        Log.i(TAG, "哈哈哈");
        
    }
}
