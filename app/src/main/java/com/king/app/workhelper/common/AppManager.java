package com.king.app.workhelper.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Message;

import com.king.applib.base.WeakHandler;

import java.util.LinkedList;

/**
 * 如果应用内有多个进程，每创建一个进程就会跑一次Application的onCreate方法，每个进程内存都是独立的，
 * 所以通过这种方式无法实现将应用的Activity放在同一个LinkedList中，不能实现完全退出一个应用。
 * 判断应用程序在后台：http://steveliles.github.io/is_my_android_app_currently_foreground_or_background.html
 * Created by VanceKing on 2016/12/16 0016.
 */

public class AppManager implements Application.ActivityLifecycleCallbacks {
    private LinkedList<ActivityInfo> mExistedActivities;
    private Application mApplication;
    /*
    当A Activity跳转到B Activity时,从A的onPause()到B的onResume()方法所在的时间mInForeground = false.
    很显然是不正确的，应用应该还是在前台。通过Handler延时检查。
     */
    private boolean mInForeground = false;
    private boolean mPaused = true;
    private MyHandler mMyHandler = new MyHandler(this);

    private class MyHandler extends WeakHandler<AppManager> {

        public MyHandler(AppManager target) {
            super(target);
        }

        @Override
        public void handle(AppManager target, Message msg) {
            if (msg.what == 0) {
                if (mInForeground && mPaused) {
                    mInForeground = false;
                }
            }
        }
    }

    public static AppManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static AppManager INSTANCE = new AppManager();
    }

    public void init(Application app) {
        mApplication = app;
        app.registerActivityLifecycleCallbacks(this);
        mExistedActivities = new LinkedList<>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (null != mExistedActivities && null != activity) {
            // 把新的 activity 添加到最前面，和系统的 activity 堆栈保持一致
            mExistedActivities.offerFirst(new ActivityInfo(activity, ActivityInfo.STATE_CREATE));
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        mInForeground = true;
        mPaused = false;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mPaused = true;
        mMyHandler.sendEmptyMessageDelayed(0, 500);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (null != mExistedActivities && null != activity) {
            ActivityInfo info = findActivityInfo(activity);
            if (null != info) {
                mExistedActivities.remove(info);
            }
        }
    }

    /** 应用是否在前台 */
    public boolean isForeground() {
        return mInForeground;
    }

    public void exitAllActivity() {
        if (null == mExistedActivities) {
            return;
        }
        // 先暂停监听（省得同时在2个地方操作列表）
        mApplication.unregisterActivityLifecycleCallbacks(this);
        // 弹出的时候从头开始弹，和系统的 activity 堆栈保持一致
        for (ActivityInfo info : mExistedActivities) {
            if (info != null && info.mActivity != null) {
                try {
                    info.mActivity.finish();
                } catch (Exception e) {
                    //
                }
            }
        }
        mExistedActivities.clear();
        // 退出完之后再添加监听
        mApplication.registerActivityLifecycleCallbacks(this);
    }

    private ActivityInfo findActivityInfo(Activity activity) {
        if (null == activity || null == mExistedActivities) {
            return null;
        }
        for (ActivityInfo info : mExistedActivities) {
            if (null == info) {
                continue;
            }
            if (activity.equals(info.mActivity)) {
                return info;
            }
        }
        return null;
    }

    private class ActivityInfo {
        private final static int STATE_NONE = 0;
        private final static int STATE_CREATE = 1;
        private Activity mActivity;
        private int mState;

        ActivityInfo() {
            mActivity = null;
            mState = STATE_NONE;
        }

        ActivityInfo(Activity activity, int state) {
            mActivity = activity;
            mState = state;
        }
    }
}
