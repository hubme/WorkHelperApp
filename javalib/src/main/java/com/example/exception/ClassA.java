package com.example.exception;

/**
 * @author VanceKing
 * @since 2018/5/24.
 */
public class ClassA {
    public void method(int number) throws FirstException {
        if (number < 0) {
            throw new FirstException("FirstException");
        }
    }
}
