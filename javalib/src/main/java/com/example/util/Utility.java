package com.example.util;

import java.util.List;
import java.util.Random;

/**
 * @author VanceKing
 * @since 2017/4/16.
 */

public class Utility {
    private Utility() {
        throw new IllegalStateException("No Instance");
    }

    public static void sleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** 生成一个指定范围的随机数，[min, max] */
    public static int random(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 随机生成一个 int 数组。
     *
     * @param random 随机数生成器
     * @param size   数组大小
     * @param min    数组最大值
     * @param max    数组最小值
     * @return 数据
     */
    public static int[] generateNumbers(Random random, int size, int min, int max) {
        if (random == null || size <= 0 || min > max) {
            return null;
        }
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = random.nextInt(max - min + 1) + min;
        }
        return numbers;
    }

    /** 输出列表元素 */
    public static <E> void printList(List<E> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (E e : list) {
            System.out.println(e);
        }
        System.out.println();
    }

    /** 输出列表元素 */
    public static <E> void printList(String startMessage, List<E> list) {
        System.out.println(startMessage);
        if (list == null || list.isEmpty()) {
            return;
        }
        for (E e : list) {
            System.out.println(e);
        }
        System.out.println();
    }
}
