package com.example.thread;

import com.example.util.Utility;

/**
 * @author VanceKing
 * @since 2018/4/9.
 */
public class SynchronizedSample {
    public static void main(String[] args) {
        final AClass aClass = new AClass();
        final AClass aClass2 = new AClass();
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                aClass.printA();
            }
        };
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                aClass.printB();
            }
        }.start();
    }

    private static class AClass{
        private static final Object lock = new Object();
        public void printA() {
            synchronized (this) {
                System.out.println("A方法开始");
                Utility.sleep(2000);
                System.out.println("printA");
                System.out.println("A方法结束");
            }
        }

        public void printB() {
            synchronized (AClass.class) {
                System.out.println("B方法开始");
                System.out.println("printB");
                System.out.println("B方法结束");
            }
        }

        private void method() {
            synchronized (AClass.class) {
            }
        }
    }
}
