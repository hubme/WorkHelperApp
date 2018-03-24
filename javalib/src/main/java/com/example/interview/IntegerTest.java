package com.example.interview;

import java.lang.reflect.Field;

/**
 * @author VanceKing
 * @since 2018/3/24.
 */

public class IntegerTest {
    public static void main(String[] args) {
        IntegerTest main = new IntegerTest();
        main.integerSample1();
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
}
