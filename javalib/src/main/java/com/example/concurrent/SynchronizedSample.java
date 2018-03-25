package com.example.concurrent;

import java.util.Date;

/**
 * @author VanceKing
 * @since 2018/3/18.
 */

public class SynchronizedSample {


    public static void main(String[] args) {
        test5();
    }

    /*
    t2 on obj1 开始时间: Sun Mar 18 16:01:10 CST 2018
    t4 on obj2 开始时间: Sun Mar 18 16:01:10 CST 2018
    t3 on obj2 开始时间: Sun Mar 18 16:01:10 CST 2018
    t1 on obj1 开始时间: Sun Mar 18 16:01:10 CST 2018

    t4 on obj2 结束时间: Sun Mar 18 16:01:15 CST 2018
    t2 on obj1 结束时间: Sun Mar 18 16:01:15 CST 2018
    t1 on obj1 结束时间: Sun Mar 18 16:01:20 CST 2018
    t3 on obj2 结束时间: Sun Mar 18 16:01:20 CST 2018
     */
    private static void test1() {
        MyObject myObject1 = new MyObject();
        MyObject myObject2 = new MyObject();

        Thread thread1 = new Thread(new MyRunnable(myObject1, 1), "t1 on obj1");
        Thread thread2 = new Thread(new MyRunnable(myObject1, 1), "t2 on obj1");
        Thread thread3 = new Thread(new MyRunnable(myObject2, 1), "t3 on obj2");
        Thread thread4 = new Thread(new MyRunnable(myObject2, 1), "t4 on obj2");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    /*
t2 on obj1 开始时间: Sun Mar 18 16:16:15 CST 2018
t4 on obj2 开始时间: Sun Mar 18 16:16:15 CST 2018
t1 on obj1 开始时间: Sun Mar 18 16:16:15 CST 2018
t3 on obj2 开始时间: Sun Mar 18 16:16:15 CST 2018

t2 on obj1 结束时间: Sun Mar 18 16:16:20 CST 2018
t3 on obj2 结束时间: Sun Mar 18 16:16:25 CST 2018
t1 on obj1 结束时间: Sun Mar 18 16:16:30 CST 2018
t4 on obj2 结束时间: Sun Mar 18 16:16:35 CST 2018
 */
    private static void test2() {
        MyObject myObject1 = new MyObject();
        MyObject myObject2 = new MyObject();

        Thread thread1 = new Thread(new MyRunnable(myObject1, 2), "t1 on obj1");
        Thread thread2 = new Thread(new MyRunnable(myObject1, 2), "t2 on obj1");
        Thread thread3 = new Thread(new MyRunnable(myObject2, 2), "t3 on obj2");
        Thread thread4 = new Thread(new MyRunnable(myObject2, 2), "t4 on obj2");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    /*
    t3 on obj2 开始时间: Sun Mar 18 16:25:18 CST 2018
t1 on obj1 开始时间: Sun Mar 18 16:25:18 CST 2018
t4 on obj2 开始时间: Sun Mar 18 16:25:18 CST 2018
t2 on obj1 开始时间: Sun Mar 18 16:25:18 CST 2018

t3 on obj2 结束时间: Sun Mar 18 16:25:23 CST 2018
t1 on obj1 结束时间: Sun Mar 18 16:25:23 CST 2018
t4 on obj2 结束时间: Sun Mar 18 16:25:28 CST 2018
t2 on obj1 结束时间: Sun Mar 18 16:25:28 CST 2018
     */
    private static void test3() {
        MyObject myObject1 = new MyObject();
        MyObject myObject2 = new MyObject();

        Thread thread1 = new Thread(new MyRunnable(myObject1, 3), "t1 on obj1");
        Thread thread2 = new Thread(new MyRunnable(myObject1, 3), "t2 on obj1");
        Thread thread3 = new Thread(new MyRunnable(myObject2, 3), "t3 on obj2");
        Thread thread4 = new Thread(new MyRunnable(myObject2, 3), "t4 on obj2");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    /*
    t1 on obj1 开始时间: Sun Mar 18 16:49:05 CST 2018
t4 on obj2 开始时间: Sun Mar 18 16:49:05 CST 2018
t2 on obj1 开始时间: Sun Mar 18 16:49:05 CST 2018
t3 on obj2 开始时间: Sun Mar 18 16:49:05 CST 2018

t4 on obj2 结束时间: Sun Mar 18 16:49:10 CST 2018
t1 on obj1 结束时间: Sun Mar 18 16:49:10 CST 2018
t2 on obj1 结束时间: Sun Mar 18 16:49:15 CST 2018
t3 on obj2 结束时间: Sun Mar 18 16:49:15 CST 2018
     */
    private static void test4() {
        MyObject myObject1 = new MyObject();
        MyObject myObject2 = new MyObject();

        Thread thread1 = new Thread(new MyRunnable(myObject1, 4), "t1 on obj1");
        Thread thread2 = new Thread(new MyRunnable(myObject1, 4), "t2 on obj1");
        Thread thread3 = new Thread(new MyRunnable(myObject2, 4), "t3 on obj2");
        Thread thread4 = new Thread(new MyRunnable(myObject2, 4), "t4 on obj2");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    /*
    t4 on obj2 开始时间: Tue Mar 20 10:24:54 CST 2018
t1 on obj1 开始时间: Tue Mar 20 10:24:54 CST 2018
t2 on obj1 开始时间: Tue Mar 20 10:24:54 CST 2018
t3 on obj2 开始时间: Tue Mar 20 10:24:54 CST 2018

t4 on obj2 结束时间: Tue Mar 20 10:24:59 CST 2018
t3 on obj2 结束时间: Tue Mar 20 10:25:04 CST 2018
t2 on obj1 结束时间: Tue Mar 20 10:25:09 CST 2018
t1 on obj1 结束时间: Tue Mar 20 10:25:14 CST 2018
     */
    private static void test5() {
        MyObject myObject1 = new MyObject();
        MyObject myObject2 = new MyObject();

        Thread thread1 = new Thread(new MyRunnable(myObject1, 5), "t1 on obj1");
        Thread thread2 = new Thread(new MyRunnable(myObject1, 5), "t2 on obj1");
        Thread thread3 = new Thread(new MyRunnable(myObject2, 5), "t3 on obj2");
        Thread thread4 = new Thread(new MyRunnable(myObject2, 5), "t4 on obj2");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}

class MyObject {
    private int count = 0;
    private static int count2 = 0;
    private Object lock = new Object();

    public MyObject() {
        synchronized (this) {
            
        }
    }

    //实例同步方法。多个线程同时访问时，只能有一个线程允许进入同步方法。
    public synchronized void add(int value) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.count += value;
    }

    //静态同步方法。由于静态方法是属于类的，在当前类中只允许一个线程访问。
    public static synchronized void add2(int value) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count2 += value;
    }

    //必须获得对象的监视器才能访问。实例方法的同步加上代码块this的同步，仍然针对同一个实例对象
    public void add3(int value) {
        synchronized (this) {//"this" 监视器对象是当前类的实例。
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.count += value;
        }
    }

    public void add4(int value) {
        synchronized (lock) {//自定义监视器对象
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.count += value;
        }
    }
    
    public void add5(int value) {
        synchronized (MyObject.class) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.count += value;
        }
    }

}

interface AAA{
    int a = 1;
    void test();
}

class MyRunnable implements Runnable {
    private MyObject myObject;
    private int method;

    public MyRunnable(MyObject myObject, int method) {
        this.myObject = myObject;
        this.method = method;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 开始时间: " + new Date());
        if (method == 1) {
            myObject.add(1);
        } else if (method == 2) {
            MyObject.add2(1);
        } else if (method == 3) {
            myObject.add3(1);
        } else if (method == 4) {
            myObject.add4(1);
        }else if (method == 5) {
            myObject.add5(1);
        }
        System.out.println(Thread.currentThread().getName() + " 结束时间: " + new Date());
    }
}