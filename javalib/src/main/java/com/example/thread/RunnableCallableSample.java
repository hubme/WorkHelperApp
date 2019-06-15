package com.example.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author VanceKing
 * @since 2018/3/27.
 */

class RunnableCallableSample {
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        RunnableCallableSample main = new RunnableCallableSample();
        main.futureTaskSample1();

    }

    private void futureTaskSample2() throws ExecutionException, InterruptedException {
        //FutureTask 构造函数中调用Executors.callable()方法，通过 Executors.RunnableAdapter(RunnableAdapter<T> implements Callable<T>) 转换成Callable执行
        FutureTask<String> futureTask = new FutureTask<>(new Runnable() {
            @Override
            public void run() {
                printResult();
            }
        }, "id-2");
        Future<String> future = mExecutor.submit(futureTask, "aaa");
        //FutureTask.get() 会阻塞当前线程。返回"aaa"
        System.out.println("future result: " + future.get());
    }

    private void futureTaskSample1() throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new Runnable() {
            @Override
            public void run() {
                printResult();
            }
        }, "result return");
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println("getting result ...");
        System.out.println("future result: " + futureTask.get());//FutureTask.get() 会阻塞当前线程
        System.out.println("behind FutureTask.get()");
    }

    private void callableWithReturnNull() throws ExecutionException, InterruptedException {
        //不需要返回结果，使用 "?" 通配符。
        Future<?> result = mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                printResult();
            }
        });
        //返回 null
        System.out.println("future result : " + result.get());
    }

    private void callableWithReturnValue() throws ExecutionException, InterruptedException {
        Future<Integer> result = mExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return printResult();
            }
        });

        System.out.println("future result from callable : " + result.get());
    }

    private void runnableSample() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                printResult();
            }
        }.start();
    }

    /**
     * 效率低下的斐波那契数列, 耗时的操作
     */
    private static int fibc(int num) {
        if (num == 0) {
            return 0;
        }
        if (num == 1) {
            return 1;
        }
        return fibc(num - 1) + fibc(num - 2);
    }

    private static int printResult() {
        System.out.println("begin time: " + System.currentTimeMillis());
        int fibc = fibc(38);
        System.out.println("fibc: " + fibc);
        System.out.println("stop time: " + System.currentTimeMillis());
        return fibc;
    }
}
