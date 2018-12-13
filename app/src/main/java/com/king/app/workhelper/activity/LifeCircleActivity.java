package com.king.app.workhelper.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.king.app.workhelper.R;
import com.king.app.workhelper.constant.GlobalConstant;
import com.king.app.workhelper.fragment.LifeCircleFragment;

import butterknife.ButterKnife;

/**
 * @author VanceKing
 * @since 2018/1/15.
 */

public class LifeCircleActivity extends AppCompatActivity {
    private static final String BUNDLE_KEY = "BUNDLE_KEY";
    private boolean mIsShowFragmentLog = true;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_circle);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            log("onCreate." + savedInstanceState.getString(BUNDLE_KEY));
        } else {
            log("onCreate. savedInstanceState == null");
        }

        if (mIsShowFragmentLog) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new LifeCircleFragment())
                    .commit();
        }
    }

    @Override protected void onStart() {
        super.onStart();
        log("onStart.");
    }

    //onRestoreInstanceState被调用的前提是，activity “确实”被系统销毁了
    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            log("onRestoreInstanceState." + savedInstanceState.getString(BUNDLE_KEY));
        } else {
            log("onRestoreInstanceState. savedInstanceState == null");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log("onRestart.");
    }

    @Override protected void onResume() {
        super.onResume();
        log("onResume.");
    }

    @Override protected void onPause() {
        super.onPause();
        log("onPause.");
    }

    //将要被kill的时候回调（例如按Home进入后台、长按Home选择其他程序、锁屏、屏幕旋转前、跳转下一个Activity等情况下会被调用）
    //可以打开开发者选项中的“不保留活动”调试。主动关闭 Activity 不会调用该方法。
    //onSaveInstanceState的调用遵循一个重要原则，即当系统“未经你许可”时销毁了你的activity，则onSaveInstanceState会被系统调用，
    //因为系统有责任也应该提供机会让你保存数据。
    //see also: android.view.View.onSaveInstanceState()
    @Override protected void onSaveInstanceState(Bundle outState) {
        //注意在调用 super 之前保存数据
        outState.putString(BUNDLE_KEY, "哈哈哈");
        super.onSaveInstanceState(outState);
        log("onSaveInstanceState");
    }

    @Override protected void onStop() {
        super.onStop();
        log("onStop.");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        log("onDestroy.");
    }

    //指定android:configChanges="orientation|screenSize"，横竖屏切换是会执行onConfigurationChanged()方法。
    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        log("onConfigurationChanged");
    }

    private void log(String message) {
        Log.i(GlobalConstant.LOG_TAG, "Activity#" + message);
    }
}
