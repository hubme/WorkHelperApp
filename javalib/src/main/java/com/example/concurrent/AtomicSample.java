package com.example.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author VanceKing
 * @since 2018/5/10.
 */
public class AtomicSample {
    private static final AtomicInteger idCreator = new AtomicInteger(0);

    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        System.out.println("get(): " + idCreator.get());

        while (idCreator.get() < 100) {
            System.out.println(String.valueOf(idCreator.getAndIncrement()));
        }
    }


}
