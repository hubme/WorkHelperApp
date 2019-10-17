package com.example.concurrent.blockingqueue;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author VanceKing
 * @since 2019/10/17.
 */
class DelayQueueSample {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        DelayQueue<Message> queue = new DelayQueue<>();
        queue.offer(new Message(9, "msg_1", 1));
        queue.offer(new Message(2, "msg_2", 2));
        queue.offer(new Message(3, "msg_3", 5));
        queue.offer(new Message(4, "msg_4", 6));

        new Thread(new Consumer(queue)).start();
    }

    private static class Consumer implements Runnable {
        private DelayQueue<Message> queue;

        public Consumer(DelayQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            System.out.println("开始获取消息: " + System.currentTimeMillis());
            while (true) {
                try {
                    Message message = queue.take();
                    System.out.println("消费消息 id: " + message.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static class Message implements Delayed {
        private int id;
        private String message;
        
        private long executeTime;

        public Message() {
        }

        public Message(int id, String message, long delayDuration) {
            this.id = id;
            this.message = message;
            this.executeTime = TimeUnit.NANOSECONDS.convert(delayDuration, TimeUnit.SECONDS) + System.nanoTime();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "id=" + id +
                    ", message='" + message + '\'' +
                    ", executeTime=" + executeTime +
                    '}' + System.currentTimeMillis();
        }

        @Override
        public long getDelay(@NotNull TimeUnit unit) {
            return unit.convert(this.executeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(@NotNull Delayed o) {
            Message other = (Message) o;
            return this.id - other.id;
        }
    }
}

