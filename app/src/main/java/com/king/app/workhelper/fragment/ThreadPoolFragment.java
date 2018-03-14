package com.king.app.workhelper.fragment;

import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.king.app.workhelper.R;
import com.king.app.workhelper.common.AppBaseFragment;
import com.king.app.workhelper.constant.GlobalConstant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.OnClick;

/**
 * @author VanceKing
 * @since 2018/3/13.
 */

public class ThreadPoolFragment extends AppBaseFragment {
    private ExecutorService mSingleExecutor;
    private ExecutorService mFixedExecutor;
    private ExecutorService mCachedExecutor;
    private ScheduledExecutorService mScheduledExecutor;
    private ScheduledExecutorService mSingleScheduledExecutor;
    private ExecutorService mCustomExecutor;

    @Override protected int getContentLayout() {
        return R.layout.fragment_thread_pool;
    }

    @Override protected void initData() {
        super.initData();
        mSingleExecutor = Executors.newSingleThreadExecutor();
        mFixedExecutor = Executors.newFixedThreadPool(3);
        mCachedExecutor = Executors.newCachedThreadPool();
        mScheduledExecutor = Executors.newScheduledThreadPool(3);
        mSingleScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        mCustomExecutor = new MyThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());

    }

    @OnClick(R.id.tv_single)
    public void onSingleExecutorClick(TextView textView) {
        if (checkExecutorAvailable(mSingleExecutor)) {
            //提交10个任务，任务单步执行
            for (int i = 0; i < 10; i++) {
                mSingleExecutor.execute(new MyTask(i));
            }
        }

    }

    @OnClick(R.id.tv_fixed)
    public void onFixedExecutorClick(TextView textView) {
        if (checkExecutorAvailable(mFixedExecutor)) {
            //提交10个任务，有3个任务同步执行
            for (int i = 0; i < 10; i++) {
                mFixedExecutor.execute(new MyTask(i));
            }
        }

    }

    @OnClick(R.id.tv_cached)
    public void onCachedExecutorClick(TextView textView) {
        if (checkExecutorAvailable(mCachedExecutor)) {
            for (int i = 0; i < 10; i++) {
                SystemClock.sleep(1000);//不延时的话，10个任务会同步执行
                mCachedExecutor.execute(new MyDelayTask(i));
            }
        }
    }

    @OnClick(R.id.tv_scheduled)
    public void onScheduledExecutorClick(TextView textView) {
        if (checkExecutorAvailable(mScheduledExecutor)) {
            //三个线程周期性的、同步的执行10个任务
            for (int i = 0; i < 10; i++) {
                mScheduledExecutor.scheduleAtFixedRate(new MyTask(i), 2000, 4000, TimeUnit.MILLISECONDS);
            }
        }

    }

    @OnClick(R.id.tv_single_scheduled)
    public void onSingleScheduledClick(TextView textView) {
        if (checkExecutorAvailable(mSingleScheduledExecutor)) {
            //同一个线程周期性的执行10个任务
            for (int i = 0; i < 10; i++) {
                mSingleScheduledExecutor.scheduleAtFixedRate(new MyTask(i), 2000, 4000, TimeUnit.MILLISECONDS);
            }
        }

    }

    @OnClick(R.id.tv_custom_executor)
    public void onCustomExecutorClick(TextView textView) {
        if (checkExecutorAvailable(mCustomExecutor)) {
            //优先级大的任务先执行
            for (int i = 0; i < 5; i++) {
                mCustomExecutor.execute(new MyPriorityTask(i, i));
            }
        }
    }

    @OnClick(R.id.tv_shut_down)
    public void onExecutorShutDownClick(TextView textView) {
        shutDownExecutor();
    }

    private void shutDownExecutor() {
        mSingleExecutor.shutdown();//提交的任务会继续执行完毕;shutdownNow()时已经提交的任务也不会执行。
        mFixedExecutor.shutdown();
        mCachedExecutor.shutdown();
        mScheduledExecutor.shutdown();
        mSingleScheduledExecutor.shutdown();
        mCustomExecutor.shutdown();
    }

    //如果线程池关闭后再提交任务会抛出异常
    private boolean checkExecutorAvailable(ExecutorService executorService) {
        boolean shutdown = executorService.isShutdown();
        if (shutdown) {
            Log.i(GlobalConstant.LOG_TAG, "线程池已关闭。不能执行任务");
        }
        return !shutdown;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        shutDownExecutor();
    }

    private static class MyTask implements Runnable {
        private int id;

        public MyTask(int id) {
            this.id = id;
        }

        @Override public void run() {
            Log.i(GlobalConstant.LOG_TAG, "线程：" + Thread.currentThread().getName() + " 正在执行第 " + id + " 个任务");
            SystemClock.sleep(2000);
        }
    }

    private static class MyDelayTask implements Runnable {
        private int id;

        public MyDelayTask(int id) {
            this.id = id;
        }

        @Override public void run() {
            Log.i(GlobalConstant.LOG_TAG, "线程：" + Thread.currentThread().getName() + " 正在执行第 " + id + " 个任务");
            SystemClock.sleep(id * 500);
        }
    }

    private static class MyPriorityTask extends PriorityRunnable {
        private int id;

        public MyPriorityTask(int id, int priority) {
            super(priority);
            this.id = id;
        }

        @Override public void doWork() {
            Log.i(GlobalConstant.LOG_TAG, "线程：" + Thread.currentThread().getName() + " 正在执行第 " + id + " 个任务");
            SystemClock.sleep(2000);
        }
    }

    private static abstract class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {
        private int priority;

        public PriorityRunnable(int priority) {
            if (priority < 0)
                throw new IllegalArgumentException();
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityRunnable another) {
            int my = this.getPriority();
            int other = another.getPriority();
            return my < other ? 1 : my > other ? -1 : 0;
        }

        @Override
        public void run() {
            doWork();
        }

        public abstract void doWork();

        public int getPriority() {
            return priority;
        }
    }

    public class MyThreadPoolExecutor extends ThreadPoolExecutor {
        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            String threadName = t.getName();
            Log.i(GlobalConstant.LOG_TAG, "线程 " + threadName + " 准备执行任务");
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            String threadName = Thread.currentThread().getName();
            Log.i(GlobalConstant.LOG_TAG, "线程 " + threadName + " 任务执行结束");
        }

        @Override
        protected void terminated() {
            super.terminated();
            Log.i(GlobalConstant.LOG_TAG, "线程池已关闭");
        }
    }

    private static class PausableThreadPoolExecutor extends ThreadPoolExecutor {
        private boolean isPaused;
        private ReentrantLock pauseLock = new ReentrantLock();
        private Condition unPaused = pauseLock.newCondition();

        public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            pauseLock.lock();
            try {
                while (isPaused) unPaused.await();
            } catch (InterruptedException ie) {
                t.interrupt();
            } finally {
                pauseLock.unlock();
            }

        }

        public void pause() {
            pauseLock.lock();
            try {
                isPaused = true;
            } finally {
                pauseLock.unlock();
            }
        }

        public void resume() {
            pauseLock.lock();
            try {
                isPaused = false;
                unPaused.signalAll();
            } finally {
                pauseLock.unlock();
            }
        }
    }
}
