package com.king.app.workhelper.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 基础Activity
 * created by VanceKing at 2016/9/29
 */

public abstract class BaseActivity extends AppCompatActivity {

    private View mContentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInitialData();
        mContentLayout = LayoutInflater.from(this).inflate(getContentLayout(), null);
        setContentView(mContentLayout);
        initData();
    }

    public abstract int getContentLayout();

    private void initInitialData() {
    }

    private void initData() {

    }
}
