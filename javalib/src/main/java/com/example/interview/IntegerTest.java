package com.example.interview;

import java.lang.reflect.Field;

/**
 * @author VanceKing
 * @since 2018/3/24.
 */

public class IntegerTest {
    public static void main(String[] args) {
        IntegerTest main = new IntegerTest();
        main.sample2();
    }

    /*
    * 考察Integer 缓存和对象的引用。
    */
    private void integerSample1() {
        try {
            Class cache = Integer.class.getDeclaredClasses()[0];
            Field myCache = cache.getDeclaredField("cache");
            myCache.setAccessible(true);

            Integer[] newCache = (Integer[]) myCache.get(cache);
            newCache[132] = newCache[133];//Integer[132] = 4, Integer[133] = 5

            int a = 2;
            int b = a + a;// b = 4 = Integer[132], Integer[132] 指向 Integer[133] 的内容，Integer[133] = 5
            System.out.printf("%d + %d = %d", a, a, b);//2 + 2 = 5
        } catch (Exception ignore) {

        }

    }

    //在[-128, 127]中的会返回同一个 Integer 对象
    private void sample2() {
        int i1 = 128;
        //java在编译的时候,被翻译成 Integer i5 = Integer.valueOf(128);
        Integer i2 = 128;
        Integer i2a = 128;
        Integer i3 = new Integer(128);
        System.out.println(i1 == i2);//true
        System.out.println(i1 == i3);//true
        System.out.println(i2 == i3);//false
        System.out.println(i2 == i2a);//false，不在缓存中，创建新的对象。
        System.out.println("**************");

        int i4 = 127;
        Integer i5 = 127;
        Integer i5a = 127;
        Integer i6 = new Integer(127);
        System.out.println(i4 == i5);//true
        System.out.println(i4 == i6);//true
        System.out.println(i5 == i6);//false
        System.out.println(i5 == i5a);//true，在缓存中，复用同一个对象。
        System.out.println("**************");
    }
}
