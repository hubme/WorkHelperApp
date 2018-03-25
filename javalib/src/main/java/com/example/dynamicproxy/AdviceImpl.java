package com.example.dynamicproxy;

import java.lang.reflect.Method;

/**
 * @author VanceKing
 * @since 2017/6/13.
 */

public class AdviceImpl implements Advice{
    @Override
    public void beforeMethod(Method method) {
        System.out.println("beforeMethod." + method.getName());
    }

    @Override
    public void afterMethod(Method method) {
        System.out.println("afterMethod." + method.getName());
    }
}
