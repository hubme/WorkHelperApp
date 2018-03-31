package com.example.concurrent;

import com.example.util.Utility;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 允许一个或多个线程等待其他线程完成操作。
 *
 * 闭锁CountDownLatch和栅栏CyclicBarrier区别
 * 1. 闭锁CountDownLatch做减计数，而栅栏CyclicBarrier则是加计数。
 * 2. CountDownLatch是一次性的，CyclicBarrier可以重用。
 * 3. CountDownLatch强调一个线程等多个线程完成某件事情。CyclicBarrier是多个线程互等，等大家都完成。
 * 4. CyclicBarrier在一些场景中可以替代CountDownLatch实现类似的功能。
 *
 * @author VanceKing
 * @since 2018/3/30.
 */
public class CountDownLatchSample {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        final CountDownLatch downLatch = new CountDownLatch(2);

        //等待其它线程允许完以后我才允许
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //阻塞，直到downLatch.getCount() == 0
                    boolean await = downLatch.await(2000, TimeUnit.MILLISECONDS);//downLatch.await();
                    if (await) {
                        System.out.println("其它线程都执行完毕了，我要开始工作啦!");
                    } else {
                        System.out.println("不等他们了，我要开始工作啦!");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                Utility.sleep(3000);
                System.out.println("[Thread-1] 执行完毕了。");
                downLatch.countDown();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                Utility.sleep(4000);
                System.out.println("[Thread-2] 执行完毕了。");
                downLatch.countDown();
            }
        }.start();
    }
}
