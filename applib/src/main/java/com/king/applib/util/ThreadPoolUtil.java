package com.king.applib.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类。
 *
 * @author VanceKing
 * @since 2018/9/12.
 */
public class ThreadPoolUtil {
    public static final int sProcessorSize = Runtime.getRuntime().availableProcessors();
    public static final int sMaxThreads = 2 * sProcessorSize;
    private static final ThreadPoolExecutor sThreadPoolExecutor;

    static {
//        sThreadPoolExecutor = new ThreadPoolExecutor(3, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        sThreadPoolExecutor = new ThreadPoolExecutor(3, sMaxThreads, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(sProcessorSize));
    }

    private ThreadPoolUtil() {

    }

    public static void execute(Runnable task) {
        sThreadPoolExecutor.execute(task);
    }
}
