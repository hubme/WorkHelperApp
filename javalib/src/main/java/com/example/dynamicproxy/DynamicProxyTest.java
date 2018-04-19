package com.example.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 1. 静态代理扩展性差，接口改动后代理类也需要修改。2. 接口方法很多时要为每个接口写一个代理方法.
 * 动态代理可以解决以上问题，大大提高了系统的灵活性，方便维护。
 * 动态代理是指在运行时动态生成代理类。通过反射机制实现。
 *
 * @author VanceKing
 * @since 2017/1/8.
 */

public class DynamicProxyTest {
    /**
     * InvocationHandler负责连接代理类和委托类的中间类必须实现的接口.
     * public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h) throws IllegalArgumentException
     * loader:　　一个ClassLoader对象，定义了由哪个ClassLoader对象来对生成的代理对象进行加载
     * interfaces:　　一个Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，
     * 如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了
     * h:　　一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个InvocationHandler对象上
     */
    private static class ProxyHandler implements InvocationHandler {
        //这个就是我们要代理的真实对象
        Object originalObj;

        //给我们要代理的真实对象赋初值
        Object bind(Object originalObj) {
            this.originalObj = originalObj;
            return Proxy.newProxyInstance(originalObj.getClass().getClassLoader(),
                    originalObj.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //在代理真实对象前我们可以添加一些自己的操作
            System.out.println("before method invoke.");

            //当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
            Object returnObject = method.invoke(originalObj, args);

            System.out.println("after method invoke.");

            return returnObject;
        }
    }

    public static void main(String[] args) {
        //委托类(被代理类)
        HelloImpl helloImpl = new HelloImpl();
        //代理对象
        ProxyHandler proxyHandler = new ProxyHandler();
        IHello hello = (IHello) proxyHandler.bind(helloImpl);
        hello.sayHello();
        hello.sayHi();
        //命名方式固定，以$开头，proxy为中，最后一个数字表示对象的标号。
        System.out.println(hello.getClass().getName());//com.example.$Proxy0

        // save proxy class to root of this project, you can use jd-gui to see content of the saved file 
        String saveFileName = "$Proxy0.class";
        ProxyUtil.saveProxyClass(saveFileName, helloImpl.getClass().getSimpleName(), helloImpl.getClass().getInterfaces());
    }
}
