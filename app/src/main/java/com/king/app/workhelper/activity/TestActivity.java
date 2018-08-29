package com.king.app.workhelper.activity;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;

/**
 * @author VanceKing
 * @since 2017/12/11.
 */

public class TestActivity extends AppBaseActivity {
    @Override protected void initInitialData() {
        super.initInitialData();
    }

    @Override protected void initData() {
        //不能重复添加
//        getLifecycle().addObserver(new MyLifecycleObserver());
        getLifecycle().addObserver(new MyLifecycleObserver2());
    }

    @Override protected int getContentLayout() {
        return R.layout.activity_test;
    }

    @Override protected void initContentView() {
        super.initContentView();
    }

    //从 support-compat-26.1.0 开始支持 Lifecycle
    private static class MyLifecycleObserver implements LifecycleObserver {
        //不管什么生命周期都会执行
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        public void any(LifecycleOwner owner) {
//            Log.i("aaa", "Lifecycle.Event.ON_ANY");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void create(LifecycleOwner owner) {
            Log.i("aaa", "Lifecycle.Event.ON_CREATE");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void start(LifecycleOwner owner) {
            Log.i("aaa", "Lifecycle.Event.ON_START");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void resume(LifecycleOwner owner) {
            Log.i("aaa", "Lifecycle.Event.ON_RESUME");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void pause(LifecycleOwner owner) {
            Log.i("aaa", "Lifecycle.Event.ON_PAUSE");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void stop(LifecycleOwner owner) {
            Log.i("aaa", "Lifecycle.Event.ON_STOP");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void destroy(LifecycleOwner owner) {
            Log.i("aaa", "Lifecycle.Event.ON_DESTROY");
        }
    }

    //需要依赖android.arch.lifecycle:common-java8:1.1.1
    private static class MyLifecycleObserver2 implements DefaultLifecycleObserver {
        @Override public void onCreate(@NonNull LifecycleOwner owner) {
            Log.i("bbb", "Lifecycle.Event.ON_CREATE");

        }

        @Override public void onStart(@NonNull LifecycleOwner owner) {
            Log.i("bbb", "Lifecycle.Event.ON_START");

        }

        @Override public void onResume(@NonNull LifecycleOwner owner) {
            Log.i("bbb", "Lifecycle.Event.ON_RESUME");

        }

        @Override public void onPause(@NonNull LifecycleOwner owner) {
            Log.i("bbb", "Lifecycle.Event.ON_PAUSE");

        }

        @Override public void onStop(@NonNull LifecycleOwner owner) {
            Log.i("bbb", "Lifecycle.Event.ON_STOP");
        }

        @Override public void onDestroy(@NonNull LifecycleOwner owner) {
            Log.i("bbb", "Lifecycle.Event.ON_DESTROY");
        }
    }
}
