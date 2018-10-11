package com.king.app.workhelper.activity;

import android.content.Intent;
import android.content.IntentFilter;

import com.king.app.workhelper.R;
import com.king.app.workhelper.app.keepalive.KeepAliveReceiver;
import com.king.app.workhelper.app.keepalive.KeepAliveService;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    private KeepAliveReceiver aliveReceiver;
    private Intent mattcherIntent;

    @Override protected void initInitialData() {
        super.initInitialData();
    }

    @Override protected void initData() {
        aliveReceiver = new KeepAliveReceiver();
    }

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();


    }

    @OnClick(R.id.textView)
    public void onViewClick() {
        startService(new Intent(this, KeepAliveService.class));
    }

    @OnClick(R.id.textView2)
    public void onView2Click() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        mattcherIntent = registerReceiver(aliveReceiver, intentFilter);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (mattcherIntent != null) {
            unregisterReceiver(aliveReceiver);
        }
    }
}
