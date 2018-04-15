package com.king.app.workhelper.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.king.app.workhelper.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2018/1/15.
 */

public class LifeCircleActivity extends AppCompatActivity {
    private static final String TAG = "aaa";
    private static final String BUNDLE_KEY = "BUNDLE_KEY";

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_circle);
        ButterKnife.bind(this);
        
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart.");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume.");
    }

    @Override protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause.");
    }

    @Override protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop.");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy.");
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

    //指定android:configChanges="orientation|screenSize"，横竖屏切换是会执行onConfigurationChanged()方法。
    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("aaa", "onConfigurationChanged");
    }

    @OnClick(R.id.tv_open)
    public void onOpenClick(TextView textView) {
        openOtherActivity();
    }

    private void openOtherActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
