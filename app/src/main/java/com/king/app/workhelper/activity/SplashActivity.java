package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Splash页面
 *
 * @author huoguangxu
 * @since 2017/3/16.
 */

public class SplashActivity extends AppBaseActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_spash;
    }

    @Override
    protected void initData() {
        super.initData();
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> goHomePage());
    }

    private void goHomePage() {
        openActivity(HomeActivity.class);
        finish();
        overridePendingTransition(R.anim.alpha_appear, R.anim.alpha_disappear);
    }

}
