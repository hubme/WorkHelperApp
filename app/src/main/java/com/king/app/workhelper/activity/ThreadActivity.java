package com.king.app.workhelper.activity;

import android.os.SystemClock;
import android.util.Log;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseActivity;
import com.king.app.workhelper.constant.GlobalConstant;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2018/3/14.
 */

public class ThreadActivity extends AppBaseActivity {

    private MyThread mThread;
    private MyThreadRunnable mThreadRunnable;
    private Thread mThread2;

    @Override protected int getContentLayout() {
        return R.layout.activity_thread;
    }

    @Override protected String getActivityTitle() {
        return "线程";
    }

    @Override protected void initData() {
        super.initData();
        mThread = new MyThread();
        mThreadRunnable = new MyThreadRunnable("任务1");
        mThread2 = new Thread(mThreadRunnable);
    }

    @OnClick(R.id.tv_start_thread)
    public void onThreadStartClick() {
        startThread(mThread);
        startThread(mThread2);

    }

    private static class MyThread extends Thread {

        public MyThread() {
            super();
        }

        public MyThread(String name) {
            super(name);
        }

        @Override public void run() {
            super.run();
            log("线程 " + getName() + " 正在运行。");
            SystemClock.sleep(2000);
        }
    }

    private static class MyThreadRunnable implements Runnable {
        private String name;

        public MyThreadRunnable(String name) {
            this.name = name;
        }

        @Override public void run() {
            log("任务 " + name + " 正在执行。");
            SystemClock.sleep(2000);
        }
    }

    private void startThread(Thread thread) {
        if (isThreadAvailable(thread)) {
            thread.start();//线程只能运行一次，否则会报IllegalThreadStateException: Thread already started
        } else {
            Log.i(GlobalConstant.LOG_TAG, thread.getName() + " 不能启动线程");
        }
    }

    private boolean isThreadAvailable(Thread thread) {
        return thread.getState() == Thread.State.NEW;
    }

    private static void log(String message) {
        Log.i(GlobalConstant.LOG_TAG, message);
    }
}
