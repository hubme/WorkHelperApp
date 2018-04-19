package com.example.dynamicproxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 静态代理示例。
 */
public class StaticProxy {
    public static void main(String[] args) {
        test2a();
    }

    private static void test1() {
        Subject subject = (Subject) Proxy.newProxyInstance(Subject.class.getClassLoader(), new Class[]{Subject.class}, new InvocationHandler() {
            @Override public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return null;
            }
        });

        subject.request();

    }

    private static void test2() {
        //创建被代理对象
        RealSubject realSubject = new RealSubject();
        //创建被代理对象
        ProxySubject proxySubject = new ProxySubject(realSubject);
        proxySubject.request();
    }

    private static void test2a() {
        //1.创建目标对象
        RealSubject realSubject = new RealSubject();
        System.out.println(realSubject.toString());
        //2.创建调用处理器对象
        ProxyHandler proxyHandler = new ProxyHandler(realSubject);
        //3.动态生成代理对象
        Subject proxySubject = (Subject) Proxy.newProxyInstance(RealSubject.class.getClassLoader(), RealSubject.class.getInterfaces(), proxyHandler);
        //4.通过代理对象调用方法
        proxySubject.request();
    }

    private static void test3() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //创建一个InvocationHandler对象
        InvocationHandler handler = new InvocationHandler() {
            @Override public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return null;
            }
        };
        //使用Proxy生成一个动态代理类
        Class<?> proxyClass = Proxy.getProxyClass(RealSubject.class.getClassLoader(), RealSubject.class.getInterfaces());
        //获取代理类中一个带InvocationHandler参数的构造器
        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
        //调用constructor的newInstance方法来创建动态实例
        RealSubject realSubject = (RealSubject) constructor.newInstance(handler);

        Subject o = (Subject) Proxy.newProxyInstance(RealSubject.class.getClassLoader(), RealSubject.class.getInterfaces(), handler);
    }

    /*
      实现代理必须要有接口，接口相当于代理对象和真实对象之间的协议，
      有了协议（接口方法）才可以通过代理对象调用真实对象的方法。
     */
    private interface Subject {
        void request();
    }

    //真实对象或被代理对象
    private static class RealSubject implements Subject {

        @Override
        public void request() {
            System.out.println("real request()");
        }
    }

    //代理对象。在不侵入代码的情况下添加一些操作。
    private static class ProxySubject implements Subject {
        private Subject mRealSubject;

        public ProxySubject(Subject realSubject) {
            this.mRealSubject = realSubject;
        }

        @Override
        public void request() {
            System.out.println("before method!");
            //可以在调用代理对象方法前后执行一些其他操作。
            mRealSubject.request();
            System.out.println("after method!");
        }
    }

    private static class ProxyHandler implements InvocationHandler {
        private Subject subject;

        public ProxyHandler(Subject subject) {
            this.subject = subject;
        }

        @Override public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            System.out.println(o.toString());
            //定义预处理的工作，当然你也可以根据 method 的不同进行不同的预处理工作
            System.out.println("====before====");

            Object invoke = method.invoke(subject, objects);

            System.out.println("====after====");
            return invoke;
        }
    }
}
