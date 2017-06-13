package com.example.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/6/13 0013.
 */

public class ProxyUtil {
    public static Object getProxy(final Object target, final Advice advice) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                advice.beforeMethod(method);
                Object proxyObject = method.invoke(target, objects);
                advice.afterMethod(method);
                return proxyObject;
            }

        });
    }

    public static void main(String[] args) {
        List<String> aaa = new ArrayList<>();
        Collection proxy = (Collection) getProxy(aaa, new AdviceImpl());
        proxy.add("aaa");

    }
}
