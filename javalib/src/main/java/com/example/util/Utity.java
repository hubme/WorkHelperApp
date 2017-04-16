package com.example.util;

/**
 * @author VanceKing
 * @since 2017/4/16 0016.
 */

public class Utity {
    private void Utity() {
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
