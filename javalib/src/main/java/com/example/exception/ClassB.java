package com.example.exception;

/**
 * @author VanceKing
 * @since 2018/5/24.
 */
public class ClassB extends ClassA {
    @Override public void method(int number) throws FirstException {
        super.method(number);
        //编译失败，不能抛出比父类更多的异常
        /*if (number == 0) {
            throw new SecondException("");
        }*/
    }
}
