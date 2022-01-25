package com.example;

import org.openjdk.jol.info.ClassLayout;

import java.lang.reflect.Array;
import java.util.Random;

/**
 * @author VanceKing
 * @since 2018/4/1.
 */
public class Other {
    public static void main(String[] args) {
        Object obj = new Object();
        String layout = ClassLayout.parseInstance(obj).toPrintable();
        System.out.println(layout);
    }

    private static void printHelloWorldRandom() {
        testGenericArray();
    }

    private static void testGenericArray() {
        String[] array = (String[]) Array.newInstance(String.class, 4);
        array[0] = "A";
        array[1] = "B";
        printArray(array);

        Integer[] array2 = (Integer[]) Array.newInstance(Integer.class, 3);
        array2[0] = 0;
        array2[1] = 1;
        printArray(array2);
    }

    private static <T> void printArray(T[] array) {
        for (T t : array) {
            if (t != null) {
                System.out.println(t.toString());
            }
        }

    }

    //System.out.println(randomString(-229985452) + " " + randomString(-147909649));//output: hello world
    private static String randomString(int i) {
        Random ran = new Random(i);
        StringBuilder sb = new StringBuilder();
        while (true) {
            int k = ran.nextInt(27);
            System.out.println("char:" + k + ",number:" + k);
            if (k == 0)
                break;
            k += 96;
            sb.append((char) k);
        }

        return sb.toString();
    }
}
