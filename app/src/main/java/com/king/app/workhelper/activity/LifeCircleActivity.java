package com.king.app.workhelper.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        log("onResumeFragments.");
    }

    /*
    https://stackoverflow.com/questions/42095705/activity-life-cycle-methods-onpostresume-significance

    1. It will ensure that screen is visible to user and will do the final set up for activity.
    2. Remove any pending posts of messages with code 'what' that are in the message queue.
    3. Check all fragment gets resumed and Moves all Fragments managed by the controller's FragmentManager into the resume state.
    4. Execute any pending actions for the Fragments managed by the controller's FragmentManager.
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        log("onPostResume.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("onStart.");
    }

    // 在 onStart() 与 onPostCreate() 之间被调用。onPostCreate() 在 onResume() 之前调用。
    //onRestoreInstanceState被调用的前提是，activity “确实”被系统销毁了
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
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

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause.");
    }

    // >=9.0: 在 onStop() 后调用；< 9.0: onStop() 之前，但是不确定是在 onPause() 前后。
    //将要被kill的时候回调（例如按Home进入后台、长按Home选择其他程序、锁屏、屏幕旋转前、跳转下一个Activity等情况下会被调用）
    //可以打开开发者选项中的“不保留活动”调试。主动关闭 Activity 不会调用该方法。
    //onSaveInstanceState的调用遵循一个重要原则，即当系统“未经你许可”时销毁了你的activity，则onSaveInstanceState会被系统调用，
    //因为系统有责任也应该提供机会让你保存数据。
    //see also: android.view.View.onSaveInstanceState()
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //注意在调用 super 之前保存数据
        outState.putString(BUNDLE_KEY, "哈哈哈");
        super.onSaveInstanceState(outState);
        log("onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("onStop.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("onDestroy.");
    }

    //Activity#isChangingConfigurations() 判断 Config 是否变化了
    //指定android:configChanges="orientation|screenSize"，横竖屏切换是会执行onConfigurationChanged()方法。
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        log("onConfigurationChanged");
    }

    //屏幕旋转重新创建了对象，通过此方法获取前一个 Activity 里的对象，给新创建的 Activity 重新赋值
    //ActivityThread#RELAUNCH_ACTIVITY 消息 -> ActivityThread#handleRelaunchActivity() -> ActivityThread#handleDestroyActivity(..., true)
    //ActivityThread#performDestroyActivity(..., true) -> Activity#retainNonConfigurationInstances()
    //Activity#onRetainNonConfigurationInstance() -> FragmentActivity#onRetainCustomNonConfigurationInstance()
    //Activity#NonConfigurationInstances 静态内部类
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return null;//通过getLastCustomNonConfigurationInstance();
    }

    private void log(String message) {
        Log.i(GlobalConstant.LOG_TAG, "Activity#" + message);
    }
}
