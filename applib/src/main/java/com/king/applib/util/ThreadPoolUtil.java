package com.king.applib.util;

import java.util.concurrent.ExecutorService;
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
    private static final ExecutorService sExecutorService;

    static {
//        //#1 没有核心线程池，提交任务时要临时创建新的线程。任务完成后回收线程。
        //问题：1）每次提交任务都要创建临时线程，任务完成后回收线程。
        //     2) 当短时间内提交大量任务时，短时间内将创建大量的线程，占用系统大量内存，可能导致 OOM。
//        sExecutorService = Executors.newCachedThreadPool();

        //#2 3个好核心线程池。提交任务时当核心线程池空闲时任务立即调度执行，否则创建新的线程执行任务。任务全部完成后回收非核心线程池，保留3个核心线程
        //问题：解决了#1 的问题 1)
//        sExecutorService = new ThreadPoolExecutor(3, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

        //#3 解决了#1 的问题 1)  和 2) 
        // 问题: 1) 当核心线程数、最大线程数和任务队列都达到最大值时，再提交任务会抛出 RejectedExecutionException.RejectedExecutionException 异常。
//        sExecutorService = new ThreadPoolExecutor(2, 5, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3));

        //解决#3 的问题 1)。综合考虑使用如下配置
        //问题：1) 同时提交很多任务，但是只会执行前8个，其他任务丢弃。

        //3个核心线程池不会被回收。如果线程池中 sMaxThreads 个线程都处于活动状态时，再提交任务时任务会被丢弃。
        // 如果线程池中 3 个线程都处于活动状态时，再提交小于2个任务时会放入任务队列，然后核心线程池空闲时会执行任务队列中的任务。再提交

        //tasks 代表提交任务的个数。core 代表核心线程数。max 代表最大线程数。queue 代表任务队列最大容量。
        //1. task <= core: 创建 core 个线程执行任务
        //2. core < task <= core + queue：创建 core 个线程执行任务，task - core 个任务放入任务队列
        //3. core + queue < task <= max:  创建 core 个线程执行任务，queue 个任务放入任务队列。创建 task - core - queue 个非核心线程按 FIFO 优先级执行任务队列中的任务
        //4. max < task: 创建 core 个线程执行任务，queue 个任务放入任务队列。创建 task - core - queue 个非核心线程按 FIFO 优先级执行任务队列中的任务。task - max 个任务在被丢弃
        sExecutorService = new ThreadPoolExecutor(3, Math.max(10, sMaxThreads), 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(2), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    private ThreadPoolUtil() {

    }

    //避免意外执行 ExecutorService.shutdown() 不提供访问 ExecutorService 对象。
    private static ExecutorService getExecutorService() {
        return sExecutorService;
    }

    public static void execute(Runnable task) {
        sExecutorService.execute(task);
    }

    //重写rejectedExecution() 方法，避免抛出异常。
    private static class QuietAbortPolicy extends ThreadPoolExecutor.AbortPolicy {
        @Override public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            System.err.println("ThreadPoolExecutor.AbortPolicy.rejectedExecution。" + "Runnable: " + r.toString() + " ThreadPoolExecutor: " + e.toString());
        }
    }
}