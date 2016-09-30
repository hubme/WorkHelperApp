package com.king.app.workhelper.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.applib.log.Logger;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppBaseActivity {
    @BindView(R.id.hello_world)
    TextView mHelloView;


    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick(R.id.hello_world)
    public void printHelloWorld() {
        startViewPagerActivity();
    }

    @Subscribe
    public void receiveHaHa(String text) {
        Logger.i("text: " + text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startViewPagerActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.BUNDLE_PARAMS_KEY.EXTRA_KEY, "aaa");
        startActivity(bundle, ViewPagerActivity.class);

    }
}
