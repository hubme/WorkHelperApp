package com.example.util;

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
}
