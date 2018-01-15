package com.king.app.workhelper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2018/1/15.
 */

public class LifeCircleActivity extends AppBaseActivity {
    private static final String TAG = "aaa";
    private static final String BUNDLE_KEY = "BUNDLE_KEY";

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.i(TAG, "onCreate." + savedInstanceState.getString(BUNDLE_KEY));
        } else {
            Log.i(TAG, "onCreate. savedInstanceState == null");
        }
    }

    @Override protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart.");
    }

    @Override protected int getContentLayout() {
        return R.layout.activity_life_circle;
    }


    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Log.i(TAG, "onRestoreInstanceState." + savedInstanceState.getString(BUNDLE_KEY));
        } else {
            Log.i(TAG, "onRestoreInstanceState. savedInstanceState == null");
        }
    }

    //将要被kill的时候回调（例如进入后台、屏幕旋转前、跳转下一个Activity等情况下会被调用）。
    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_KEY, "哈哈哈");
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @OnClick(R.id.tv_open)
    public void onOpenClick(TextView textView) {
        openActivity(MainActivity.class);
    }

}
