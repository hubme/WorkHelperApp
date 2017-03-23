package com.king.app.workhelper.activity;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        goHomePage();
                    }
                });
    }

    private void goHomePage() {
        openActivity(HomeActivity.class);
        finish();
        overridePendingTransition(R.anim.alpha_appear, R.anim.alpha_disappear);
    }

}
