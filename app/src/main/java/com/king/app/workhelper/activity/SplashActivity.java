package com.king.app.workhelper.activity;

import android.os.Message;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.applib.base.WeakHandler;

/**
 * Splash页面
 *
 * @author huoguangxu
 * @since 2017/3/16.
 */

public class SplashActivity extends AppBaseActivity {

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends WeakHandler<SplashActivity> {

        private MyHandler(SplashActivity target) {
            super(target);
        }

        @Override public void handle(SplashActivity target, Message msg) {
            target.gotoHomePage();
        }
    }

    @Override public int getContentLayout() {
        return R.layout.activity_spash;
    }

    @Override protected void initData() {
        super.initData();
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void gotoHomePage() {
        openActivity(HomeActivity.class);
        finish();
        overridePendingTransition(R.anim.alpha_appear, R.anim.alpha_disappear);
    }

}
