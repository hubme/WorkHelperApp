package com.king.app.workhelper;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.junit.Test;

import java.util.Map;

/**
 * @author VanceKing
 * @since 2018/5/23.
 */
public class ArrayMapTest extends BaseAndroidJUnit4Test {

    @Test
    public void test1() {
        ArrayMap<String, Person> arrayMap = new ArrayMap<>();
        arrayMap.put("aaa", new Person("A1", "111111"));
        arrayMap.put("bbb", new Person("A2", "111112"));
        arrayMap.put("ccc", new Person("A3", "111113"));

//        arrayMap.put("ddd", new Person("A4", "111114"));
//        arrayMap.put("eee", new Person("A5", "111115"));
//        arrayMap.put("fff", new Person("A6", "111116"));
//        arrayMap.put("ggg", new Person("A7", "111117"));
//        arrayMap.put("hhh", new Person("A8", "111118"));
//        arrayMap.put("iii", new Person("A9", "111119"));


        arrayMap.remove("bbb");
        arrayMap.remove("aaa");
        arrayMap.remove("ccc");
//
//        printArrayMap(arrayMap);
    }

    private static <K, V> void printArrayMap(ArrayMap<K, V> arrayMap) {
        if (arrayMap == null || arrayMap.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : arrayMap.entrySet()) {
            sb.append(entry.getKey()).append(" --- ").append(entry.getValue()).append("\n");
        }
        Log.i(TAG, sb.toString());

    }

    private static class Person {
        private String name;
        private String idNumber;

        public Person(String name, String idNumber) {
            this.name = name;
            this.idNumber = idNumber;
        }

        @Override public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", idNumber='" + idNumber + '\'' +
                    '}';
        }
    }
}
