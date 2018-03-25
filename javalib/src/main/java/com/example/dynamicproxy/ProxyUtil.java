package com.example.dynamicproxy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

/**
 * @author VanceKing
 * @since 2017/6/13.
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

    /**
     * Save proxy class to path.<br/>
     * https://github.com/android-cn/android-open-project-demo/blob/master/java-dynamic-proxy/src/com/codekk/java/test/dynamicproxy/util/ProxyUtils.java
     *
     * @param path           path to save proxy class
     * @param proxyClassName name of proxy class
     * @param interfaces     interfaces of proxy class
     */
    public static boolean saveProxyClass(String path, String proxyClassName, Class[] interfaces) {
        if (proxyClassName == null || path == null) {
            return false;
        }

        // get byte of proxy class 
        byte[] classFile = ProxyGenerator.generateProxyClass(proxyClassName, interfaces);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(classFile);
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
