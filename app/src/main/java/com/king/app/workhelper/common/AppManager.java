package com.king.app.workhelper.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;

/**
 * 如果应用内有多个进程，每创建一个进程就会跑一次Application的onCreate方法，每个进程内存都是独立的，
 * 所以通过这种方式无法实现将应用的Activity放在同一个LinkedList中，不能实现完全退出一个应用。
 * Created by VanceKing on 2016/12/16 0016.
 */

public class AppManager implements Application.ActivityLifecycleCallbacks {
    private LinkedList<ActivityInfo> mExistedActivities = new LinkedList<>();
    private Application mApplication;

    public static AppManager getInstance() {
        return AppManagerHolder.INSTANCE;
    }

    private static class AppManagerHolder {
        private static AppManager INSTANCE = new AppManager();
    }

    public void init(Application app) {
        mApplication = app;
        app.registerActivityLifecycleCallbacks(this);
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

    }

    @Override
    public void onActivityPaused(Activity activity) {

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

    public void exitAllActivity() {
        if (null == mExistedActivities) {
            return;
        }
        // 先暂停监听（省得同时在2个地方操作列表）
        mApplication.unregisterActivityLifecycleCallbacks(this);
        // 弹出的时候从头开始弹，和系统的 activity 堆栈保持一致
        for (ActivityInfo info : mExistedActivities) {
            if (null == info || null == info.mActivity) {
                continue;
            }
            try {
                info.mActivity.finish();
            } catch (Exception e) {
                //
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
