package com.example.interview;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author VanceKing
 * @since 2019/6/19.
 */
class Test {
    public static void main(String[] args) {

    }

    private static void test1() {
        float a = 1.0f - 0.9f;
        float b = 0.9f - 0.8f;
        System.out.println("1.0f - 0.9f = " + a);//0.100000024
        System.out.println("0.9f - 0.8f = " + b);//0.099999964
    }

    private static void test2() {
        Float a = 1.0f - 0.9f;
        Float b = 0.9f - 0.8f;
        System.out.println("1.0f - 0.9f = " + a);//0.100000024
        System.out.println("0.9f - 0.8f = " + b);//0.099999964
    }

    //java.lang.NullPointerException
    private static void test3() {
        String param = null;
        switch (param) {
            case "null":
                System.out.println("null");
                break;
            default:
                System.out.println("default");
                break;
        }
    }

    private static void test4() {
        /*char[] chars = "0.1".toCharArray();
        for (char car : chars) {
            System.out.println(car);
        }*/
        BigDecimal a = new BigDecimal(0.1);
        BigDecimal b = new BigDecimal("0.1");//"0.1"
        //0.1000000000000000055511151231257827021181583404541015625
        System.out.println(a);
        //0.1
        System.out.println(b);
    }

    /*
    1. lock 是非公平锁
    2. finally 代码块不会抛出异常
    3. tryLock 获取锁失败则直接往下执行
     */
    private static void test5() {
        final Lock lock = new ReentrantLock();//默认非公平锁
        try {
            lock.tryLock();
//            lock.tryLock(3000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
