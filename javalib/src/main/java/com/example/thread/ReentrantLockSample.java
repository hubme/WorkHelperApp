package com.example.thread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockSample {

    public static void main(String[] args) {
        ReentrantLockSample sample = new ReentrantLockSample();
        sample.test1();
    }

    private void test1() {
        final int maxSize = 10; // 缓冲区最大容量
        Queue<Integer> buffer = new LinkedList<>(); // 缓冲区队列
        Lock lock = new ReentrantLock(); // 创建 ReentrantLock 实例
        Condition notFull = lock.newCondition(); // 获取 notFull Condition 对象
        Condition notEmpty = lock.newCondition(); // 获取 notEmpty Condition 对象
        Random random = new Random();

        // 生产者线程
        Thread producerThread = new Thread(() -> {
            while (true) {
                lock.lock(); // 加锁
                try {
                    while (buffer.size() == maxSize) { // 缓冲区满了，等待 notFull Condition
                        notFull.await();
                    }
                    int item = random.nextInt();
                    buffer.add(item);
                    System.out.println("Produced: " + item);
                    notEmpty.signal(); // 唤醒等待 notEmpty Condition 的线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock(); // 解锁
                }
            }
        });

        // 消费者线程
        Thread consumerThread = new Thread(() -> {
            while (true) {
                lock.lock(); // 加锁
                try {
                    while (buffer.isEmpty()) { // 缓冲区空了，等待 notEmpty Condition
                        notEmpty.await();
                    }
                    int item = buffer.poll();
                    System.out.println("Consumed: " + item);
                    notFull.signal(); // 唤醒等待 notFull Condition 的线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock(); // 解锁
                }
            }
        });

        // 启动生产者和消费者线程
        producerThread.start();
        consumerThread.start();
    }

}

