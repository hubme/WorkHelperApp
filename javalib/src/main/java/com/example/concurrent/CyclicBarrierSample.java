package com.example.concurrent;

import com.example.util.Utility;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier，让一组线程到达一个同步点后再一起继续运行，
 * 在其中任意一个线程未达到同步点，其他到达的线程均会被阻塞。
 *
 * @author VanceKing
 * @since 2018/3/30.
 */
class CyclicBarrierSample {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("都就绪了，开干！！！");
            }
        });
        Thread thread1 = new Thread(new Worker("[Thread-1] ", 4000, barrier));
        thread1.start();

        Thread thread2 = new Thread(new Worker("[Thread-2] ", 2000, barrier));
        thread2.start();

        Thread thread3 = new Thread(new Worker("[Thread-3] ", 3000, barrier));
        thread3.start();
    }

    private static class Worker implements Runnable {
        private String name;
        private CyclicBarrier barrier;
        private long duration;

        public Worker(String name, long duration, CyclicBarrier barrier) {
            this.name = name;
            this.duration = duration;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            Utility.sleep(duration);
            try {
                System.out.println(name + "已就绪！");
                barrier.await();
                Utility.sleep(duration);
                System.out.println(name + "经过 " + duration + " 完成任务！");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
