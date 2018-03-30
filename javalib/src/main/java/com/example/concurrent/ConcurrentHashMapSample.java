package com.example.concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author VanceKing
 * @since 2018/3/30.
 */

public class ConcurrentHashMapSample {
    public static void main(String[] args) {
        test1();
    }

    //不存在 HashMap 或 List 中的 ConcurrentModificationException。
    private static void test1() {
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");

        for (Map.Entry<Integer, String> entry: map.entrySet()) {
            System.out.println("key = " + entry.getKey() + " value = " + entry.getValue());
            map.put(4, "d");
        }
    }
}
