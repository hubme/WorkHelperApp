package com.example.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author VanceKing
 * @since 2018/3/23.
 */

public class MapSample {
    public static void main(String[] args) {
        test1();
        
    }

    /*
    由1和2可知Map中最多一个 null key。由3和4可知允许多个null value。
     */
    private static void test1() {
        Map<String, String> mapObject = new HashMap<>();
        mapObject.put("0", "aaa");
        mapObject.put("1", "bbb");
        mapObject.put(null, null);//1
        printMap(mapObject);
        System.out.println("=============================");
        mapObject.put(null, "ccc");//2
        mapObject.put("2", null);//3
        mapObject.put("3", null);//4
        printMap(mapObject);
    }

    private static void printMap(Map<?, ?> map) {
        Set<? extends Map.Entry<?, ?>> entries = map.entrySet();
        for (Map.Entry<?, ?> entry : entries) {
            System.out.println("key = " + entry.getKey() + " value = " + entry.getValue());
        }
    }
}
