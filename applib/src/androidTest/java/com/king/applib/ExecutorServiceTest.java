package com.king.applib;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author huoguangxu
 * @since 2017/5/9.
 */

public class ExecutorServiceTest extends BaseTestCase{

    private ScheduledExecutorService mSingleScheduled;

    @Override protected void setUp() throws Exception {
        super.setUp();
        mSingleScheduled = Executors.newSingleThreadScheduledExecutor();
    }

    public void testSingleThreadScheduledExecutor() throws Exception{
//        mSingleScheduled.scheduleWithFixedDelay(new LoopTask(), 2000, 2000, TimeUnit.MILLISECONDS);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new LoopTask(), 0, 1000, TimeUnit.MILLISECONDS);
    }
    
    private class LoopTask implements Runnable{

        @Override public void run() {
            Log.i(TAG, "aaa");
        }
    }
}
