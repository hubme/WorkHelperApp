package com.example.concurrent;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产者/消费者模型
 *
 * @author VanceKing
 * @since 2017/4/16.
 */

public class ProducerConsumerExampleWithWaitAndNotify {
    public static void main(String[] args) {
        List<Integer> taskQueue = new ArrayList<>();
        Thread tProducer = new Thread(new Producer(taskQueue, 5), "Producer");
        Thread tConsumer = new Thread(new Consumer(taskQueue), "Consumer");
        tProducer.start();
        tConsumer.start();
    }

    //生产者线程
    private static class Producer implements Runnable {
        private final List<Integer> taskQueue;
        private final int MAX_CAPACITY;

        public Producer(List<Integer> sharedQueue, int size) {
            this.taskQueue = sharedQueue;
            this.MAX_CAPACITY = size;
        }

        @Override
        public void run() {
            int counter = 0;
            while (true) {
                try {
                    produce(counter++);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void produce(int i) throws InterruptedException {
            synchronized (taskQueue) {
                while (taskQueue.size() == MAX_CAPACITY) {
                    System.out.println("Queue is full " + Thread.currentThread().getName() + " is waiting , size: " + taskQueue.size());
                    taskQueue.wait();//释放锁
                }
                Thread.sleep(1000);
                taskQueue.add(i);
                System.out.println("Produced: " + i);
                /*唤醒消费者线程.
                当taskQueue.size()<MAX_CAPACITY时，
                因为没有执行wait()，没有释放锁，即使执行notifyAll，消费者线程无法运行
                 */
                taskQueue.notifyAll();
            }
        }
    }

    //消费者线程
    private static class Consumer implements Runnable {
        private final List<Integer> taskQueue;

        public Consumer(List<Integer> sharedQueue) {
            this.taskQueue = sharedQueue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    consume();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void consume() throws InterruptedException {
            synchronized (taskQueue) {
                while (taskQueue.isEmpty()) {
                    System.out.println("Queue is empty " + Thread.currentThread().getName() + " is waiting , size: " + taskQueue.size());
                    taskQueue.wait();//释放锁
                }
                Thread.sleep(1000);
                Integer i = taskQueue.remove(0);
                System.out.println("Consumed: " + i);
                taskQueue.notifyAll();//唤醒生产者线程
            }
        }
    }
}
