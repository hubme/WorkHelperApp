package com.example;

import java.util.Random;

/**
 * @author VanceKing
 * @since 2018/4/1.
 */
public class Other {
    public static void main(String[] args) {
        printHelloWorldRandom();
    }

    private static void printHelloWorldRandom() {
        System.out.println(randomString(-229985452) + " " + randomString(-147909649));
    }

    private static String randomString(int i) {
        Random ran = new Random(i);
        StringBuilder sb = new StringBuilder();
        while (true) {
            int k = ran.nextInt(27);
            System.out.println("char:" + k + ",number:" +  k);
            if (k == 0)
                break;
            k += 96;
            sb.append((char) k);
        }

        return sb.toString();
    }
}
