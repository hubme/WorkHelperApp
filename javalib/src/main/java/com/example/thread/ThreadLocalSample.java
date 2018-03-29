package com.example.thread;

/**
 * @author VanceKing
 * @since 2018/3/29.
 */

public class ThreadLocalSample {
    ThreadLocal<String> threadLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "Vance";
        }
    };

    public static void main(String[] args) {
        ThreadLocalSample main = new ThreadLocalSample();
        main.test1();
    }

    private void test1() {
        new Thread("thread-1") {
            @Override
            public void run() {
                super.run();
                System.out.println("[thread-1] " + threadLocal.get());
                threadLocal.set("[thread-1] hello");
                System.out.println("[thread-1] " + threadLocal.get());
            }
        }.start();

        new Thread("[thread-2]") {
            @Override
            public void run() {
                super.run();
                System.out.println("[thread-2] " + threadLocal.get());
            }
        }.start();
    }
}
