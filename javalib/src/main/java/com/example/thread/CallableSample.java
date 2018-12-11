package com.example.thread;

import com.example.util.Utility;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author VanceKing
 * @since 2018/4/24.
 */

class CallableSample {
    public static void main(String[] args) throws Exception {
        sample2();
    }

    private static void sample2() throws Exception{
        MyCallable callable = new MyCallable();
        
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<String> future = service.submit(callable);
        System.out.println("线程启动了，我要获取结果了。在线程返回结果或抛出异常之前，Task 会一直阻塞。");
        System.out.println("结果为：" + future.get());
    }

    private static void sample1() throws Exception {
        //创建一个Callable 对象，代表要执行的任务
        MyCallable callable = new MyCallable();
        //通过 FutureTask 进行包装。FutureTask 实现了 Runnable 接口，可以通过 Thread 启动。
        FutureTask<String> futureTask = new FutureTask<>(callable);
        //创建 Thread ,用于启动任务
        Thread thread = new Thread(futureTask);
        //启动线程
        thread.start();

        System.out.println("线程启动了，我要获取结果了。在线程返回结果或抛出异常之前，Task 会一直阻塞。");
//        System.out.println("结果为：" + futureTask.get());
//        System.out.println("结果为：" + futureTask.get(2000, TimeUnit.MILLISECONDS));
        Utility.sleep(6000);
        System.out.println("isCancelled(): " + futureTask.isCancelled());
        System.out.println("isDone(): " + futureTask.isDone());
        //已经完成的Callable ，返回false。
        boolean cancel = futureTask.cancel(true);
        System.out.println("取消成功了吗？" + cancel);
    }

    private static class MyCallable implements Callable<String> {
        @Override public String call() throws Exception {
            System.out.println("进入了 Callable#call() 方法。");
            Utility.sleep(5000);
            return "Result";
        }
    }

}
