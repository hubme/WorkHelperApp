package com.example.dynamicproxy;

import java.lang.reflect.Method;

/**
 * @author VanceKing
 * @since 2017/6/13.
 */

public interface Advice {
    void beforeMethod(Method method);

    void afterMethod(Method method);
}
