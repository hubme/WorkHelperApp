package com.king.app.workhelper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.rx.rxlife.event.ActivityLifeEvent;
import com.king.applib.log.Logger;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Splash页面
 *
 * @author VanceKing
 * @since 2017/3/16.
 */

public class SplashActivity extends AppBaseActivity {
    @BindView(R.id.splash_screen) SimpleDraweeView mSplashImage;
    @BindView(R.id.tv_skip) TextView mSkipTv;

    private Disposable mCountDownDispose;

    @Override
    public int getContentLayout() {
        return R.layout.activity_spash;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override protected void getIntentData(Intent intent) {
        super.getIntentData(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("string: ").append(intent.getDataString()).append("\n")
                    .append("scheme: ").append(uri.getScheme()).append("\n")
                    .append("host: ").append(uri.getHost()).append("\n")
                    .append("port: ").append(uri.getPort()).append("\n")
                    .append("path: ").append(uri.getPath()).append("\n");
            Set<String> queryParameterNames = uri.getQueryParameterNames();
            for (String key : queryParameterNames) {
                sb.append(key).append(" - ").append(uri.getQueryParameter(key)).append("\n");
            }
            Logger.i(sb.toString());
        }
    }

    @Override protected void initContentView() {
        super.initContentView();
        mSplashImage.setBackgroundResource(R.drawable.img_4073);

        startCountDown(2, 0);
        setViewClickListeners(mSplashImage, mSkipTv);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(options);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @Override public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.splash_screen:

                break;
            case R.id.tv_skip:
                stopCountDown();
                goHomePage();
                break;
            default:
                break;
        }
    }

    private void goHomePage() {
        openActivityFinish(HomeActivity.class);
        overridePendingTransition(R.anim.alpha_appear, R.anim.alpha_disappear);
    }

    private void startCountDown(final long duration, long initialDelay) {
        mCountDownDispose = Observable.interval(initialDelay, 1, TimeUnit.SECONDS)
//                .doOnDispose(() -> Logger.i("doOnDispose"))
                .compose(bindUntilEvent(ActivityLifeEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .takeUntil(aLong -> {
                    boolean result = (aLong.intValue() == duration);
                    if (result) {
//                        Logger.i("倒计时结束，进入主页");
                        goHomePage();
                    }
                    return result;
                })
                .map(aLong -> duration - aLong)
                .subscribe(aLong -> mSkipTv.setText(String.format("点击跳过 %1s", aLong.intValue())));
    }

    private void stopCountDown() {
        if (mCountDownDispose != null && !mCountDownDispose.isDisposed()) {
            mCountDownDispose.dispose();
        }
    }
}
