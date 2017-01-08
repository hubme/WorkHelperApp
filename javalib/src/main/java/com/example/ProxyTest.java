package com.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author VanceKing
 * @since 2017/1/7
 */
public class ProxyTest {
    public interface ITest {
        void add(int a, int b);
    }

    public static void main(String[] args) {
        ITest iTest = (ITest) Proxy.newProxyInstance(ITest.class.getClassLoader(), new Class<?>[]{ITest.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
                Integer a = (Integer) objects[0];
                Integer b = (Integer) objects[1];
                System.out.println("method name: " + method.getName());
                System.out.println("args: " + a + ", " + b);

                return null;
            }
        });

        iTest.add(3, 5);
    }
}
