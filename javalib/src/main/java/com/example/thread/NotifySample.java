package com.example.thread;

/**
 * @author VanceKing
 * @since 2018/4/10.
 */
class NotifySample {
    private static final Object lock = new Object();
    private static final Object object = new Object();

    public static void main(String[] args){
        NotifySample sample = new NotifySample();
        sample.testProducerConsumer();

    }

    //报 IllegalMonitorStateException 异常，由于没有获取 object 对象的锁。
    //怎么才能获取到对象的锁呢？就是进入 synchronized 修饰的代码中。
    private void test1(){
//        object.wait();
        object.notify();
//        object.notifyAll();
    }

    //报异常。同 test1,虽然获取 lock 对象的锁，但是没有获取 object 对象的锁。
    private void test2(){
        synchronized (lock) {
            object.notify();
        }
    }

    //无错误。对比 test2，获取到了 object 对象的锁。
    private void test3(){
        synchronized (object) {
            object.notify();
            System.out.println("哈哈哈");
        }
    }

    //通过wait()和notify()实现生产者/消费者模型
    private void testProducerConsumer() {
        QueueBuffer q = new QueueBuffer();

        ProducerRunnable producerRunnable = new ProducerRunnable(q);
        Thread producer1 = new Thread(producerRunnable);//生产者
        producer1.setName("producer1");
        producer1.start();

        ConsumerRunnable consumerRunnable = new ConsumerRunnable(q);
        Thread consumer1 = new Thread(consumerRunnable);//消费者
        consumer1.setName("consumer1");
        consumer1.start();
    }

    //生产产品
    private static class ProducerRunnable implements Runnable {
        private QueueBuffer q;

        ProducerRunnable(QueueBuffer q) {
            this.q = q;
        }

        @Override public void run() {
            int i = 0;
            while (true) {
                if (i > 10) {
                    break;
                }
                q.put(i++);
            }
        }

    }

    //消费产品
    private static class ConsumerRunnable implements Runnable {
        private QueueBuffer q;

        ConsumerRunnable(QueueBuffer q) {
            this.q = q;
        }

        @Override public void run() {
            while (true) {
                q.get();
            }
        }

    }

    //产品操作类
    private static class QueueBuffer {
        private int n;
        private boolean valueSet = false;

        private synchronized int get() {
            if (!valueSet) {//如果没有值，则等待被唤醒
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException caught");
                }
            }
            System.out.println("Got: " + n);
            valueSet = false;
            notify();
            return n;
        }

        private synchronized void put(int n) {
            if (valueSet) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException caught");
                }
            }
            this.n = n;
            valueSet = true;
            System.out.println("Put: " + n);
            notify();
        }
    }
}
