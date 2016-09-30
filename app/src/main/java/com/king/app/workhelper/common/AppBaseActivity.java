package com.king.app.workhelper.common;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.king.applib.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * 应用基础Activity
 * Created by VanceKing on 2016/9/29.
 */

public abstract class AppBaseActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getEventBus().register(this);
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getEventBus().unregister(this);
    }
}
