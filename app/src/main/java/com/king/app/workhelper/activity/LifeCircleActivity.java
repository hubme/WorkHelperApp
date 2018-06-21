package com.king.app.workhelper.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

    //onRestoreInstanceState被调用的前提是，activity “确实”被系统销毁了
    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Log.i(TAG, "onRestoreInstanceState." + savedInstanceState.getString(BUNDLE_KEY));
        } else {
            Log.i(TAG, "onRestoreInstanceState. savedInstanceState == null");
        }
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

    //将要被kill的时候回调（例如按Home进入后台、长按Home选择其他程序、锁屏、屏幕旋转前、跳转下一个Activity等情况下会被调用）
    //可以打开开发者选项中的“不保留活动”调试。主动关闭 Activity 不会调用该方法。
    //onSaveInstanceState的调用遵循一个重要原则，即当系统“未经你许可”时销毁了你的activity，则onSaveInstanceState会被系统调用，
    //这是系统的责任，因为它必须要提供一个机会让你保存你的数据
    //see also: android.view.View.onSaveInstanceState()
    @Override protected void onSaveInstanceState(Bundle outState) {
        //注意在调用 super 之前保存数据
        outState.putString(BUNDLE_KEY, "哈哈哈");
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop.");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy.");
    }

    //指定android:configChanges="orientation|screenSize"，横竖屏切换是会执行onConfigurationChanged()方法。
    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("aaa", "onConfigurationChanged");
    }

    @OnClick(R.id.tv_open)
    public void onOpenClick(TextView textView) {
        openActivity(MainActivity.class);

    }

    //A打开透明主题的B，A不会执行onStop()方法。
    @OnClick(R.id.tv_open_transparent)
    public void onOpenTransparentClick(TextView textView) {
        openActivity(TransparentActivity.class);
    }

    //不影响生命周期
    @OnClick(R.id.tv_open_dialog)
    public void onDialogClick(TextView textView) {
        new AlertDialog.Builder(this)
                .setMessage("弹窗")
                .create()
                .show();
    }

    protected void openActivity(@NonNull Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
