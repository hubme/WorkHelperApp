package com.example.current;

import com.example.util.Utity;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BlockingQueue实现生产者消费者模型
 *
 * @author VanceKing
 * @since 2017/4/16.
 */

public class BlockingQueueSample {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);

        new Thread(new Producer(queue), "producer1").start();
        new Thread(new Producer(queue), "producer2").start();
        
        new Thread(new Consumer(queue), "consumer1").start();
        new Thread(new Consumer(queue), "consumer2").start();
    }

    private static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private Random mRandom;

        Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
            mRandom = new Random();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    queue.put(produce());
                }
            } catch (InterruptedException ex) {
                //
            }
        }

        Integer produce() {
            int anInt = mRandom.nextInt();
            System.out.println(Thread.currentThread().getName() + " produced: " + anInt);
            Utity.sleep(1000);
            return anInt;
        }
    }

    private static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;

        Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                while (true) {
                    consume(queue.take());
                }
            } catch (InterruptedException ex) {
                //
            }
        }

        void consume(Integer x) {
            System.out.println(Thread.currentThread().getName() + " consumed: " + x);
            Utity.sleep(3000);
        }
    }
}
