package com.example.current;

import com.example.util.Utity;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * BlockingQueue实现生产者消费者模型
 *
 * @author VanceKing
 * @since 2017/4/16 0016.
 */

public class ProducerConsumerBlockingQueueSample {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new SynchronousQueue<>();
        Producer producer1 = new Producer(queue);
        Producer producer2 = new Producer(queue);
        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);

        new Thread(producer1, "producer1").start();
        new Thread(producer2, "producer2").start();
        new Thread(consumer1, "consumer1").start();
        new Thread(consumer2, "consumer2").start();
    }

    private static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;

        Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

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
            int anInt = new Random().nextInt();
            System.out.println(Thread.currentThread().getName() + " produced: " + anInt);
            Utity.sleep(2000);
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
            Utity.sleep(2000);
        }
    }
}
