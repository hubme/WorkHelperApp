package com.example.thread;

import com.example.util.Utility;

/**
 * @author VanceKing
 * @since 2018/10/20.
 */
public class JoinSample {
    public static void main(String[] args) throws Exception{
        test1();
    }

    private static void test1() throws Exception{
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
}
