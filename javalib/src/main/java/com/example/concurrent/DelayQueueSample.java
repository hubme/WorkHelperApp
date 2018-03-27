package com.example.concurrent;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author VanceKing
 * @since 2018/3/6.
 */

public class DelayQueueSample {
    public static void main(String[] args) {
        // 创建延时队列  
        DelayQueue<Message> queue = new DelayQueue<>();
        Message m1 = new Message(1, "world", 1000);
        Message m2 = new Message(2, "hello", 3000);
        Message m3 = new Message(3, "呵呵呵", 6000);
        queue.add(m1);
        queue.add(m2);
        queue.add(m3);
        new Thread(new Consumer(queue)).start();
    }

    private static class Consumer implements Runnable {
        private DelayQueue<Message> queue;

        public Consumer(DelayQueue<Message> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Message take = queue.take();
                    System.out.println("custom is: " + take.getId() + ":" + take.getBody());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Message implements Delayed {

        private int id;
        private String body;  //消息内容  
        private long excuteTime;//执行时间      

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public long getExcuteTime() {
            return excuteTime;
        }

        public void setExcuteTime(long excuteTime) {
            this.excuteTime = excuteTime;
        }

        public Message(int id, String body, long delayTime) {
            this.id = id;
            this.body = body;
            this.excuteTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
        }

        @Override
        public int compareTo(Delayed delayed) {
            Message msg = (Message) delayed;
            return this.id > msg.id ? 1 : (this.id < msg.id ? -1 : 0);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.excuteTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

    }
}
