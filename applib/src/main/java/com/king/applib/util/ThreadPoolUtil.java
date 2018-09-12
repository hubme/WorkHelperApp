package com.king.applib.util;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类。
 *
 * @author VanceKing
 * @since 2018/9/12.
 */
public class ThreadPoolUtil {

    private static final ThreadPoolExecutor sThreadPoolExecutor;

    static {
//        Executors.newCachedThreadPool()
        sThreadPoolExecutor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    private ThreadPoolUtil() {

    }

    public static void execute(Runnable task) {
        sThreadPoolExecutor.execute(task);
    }
}
