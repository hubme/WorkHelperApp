package com.example.classloader;

import java.util.concurrent.TimeUnit;

/**
 * @author VanceKing
 * @since 2018/11/6.
 */
public class StaticCodeTest {
    static class DeadLoopClass {
        //代码块只执行一次
        /*static {
            if (true) {
                System.out.println(Thread.currentThread() + "init DeadLoopClass");
                while (true) {
                }
            }
        }*/

        //代码块只执行一次，线程安全
        static {
            System.out.println(Thread.currentThread() + "init DeadLoopClass");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        test();
    }

    private static void test() {
        Runnable script = new Runnable() {
            @Override public void run() {
                System.out.println(Thread.currentThread() + " start");
                DeadLoopClass dlc = new DeadLoopClass();
                System.out.println(Thread.currentThread() + " run over");
            }
        };

        Thread thread1 = new Thread(script);
        Thread thread2 = new Thread(script);
        thread1.start();
        thread2.start();
    }
}
