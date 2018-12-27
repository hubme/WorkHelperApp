package com.example.thread;

import com.example.util.Utility;

/**
 * @author VanceKing
 * @since 2018/10/20.
 */
class ThreadMethodSample {
    public static void main(String[] args) throws Exception {
        testYield();
    }

    private static void testYield() throws Exception{
        Thread thread = new Thread("aaa") {
            @Override public void run() {
                super.run();
                while (true) {
//                    System.out.println(getName()+"开始");
//                    sleepQuietly(2000);
//                    System.out.println(getName()+" - yield");
//                    Thread.yield();
//                    sleepQuietly(3000);
//                    System.out.println("aaa done!");
                }
            }
        };
        thread.start();

        new Thread("bbb"){
            @Override public void run() {
                super.run();
                System.out.println(getName()+"开始");
                sleepQuietly(1000);
                System.out.println("bbb done!");
            }
        }.start();
        
    }

    //等待调用 join() 方法的线程执行完毕后才继续执行
    private static void testNotify() throws Exception {
        Thread thread = new Thread() {
            @Override public void run() {
                System.out.println("开始进行耗时操作");
                Utility.sleep(2000);
                System.out.println("耗时操作完成");
            }
        };
        thread.setName("AAA");
        thread.start();

//        thread.join();//一直等待直到任务完成，线程die
//        thread.join(1000);//等待到超时
        thread.join(5000);
        System.out.println("我想等待" + thread.getName() + "完成后在执行。");
    }

    private static void sleepQuietly(long millTimes) {
        try {
            Thread.sleep(millTimes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
