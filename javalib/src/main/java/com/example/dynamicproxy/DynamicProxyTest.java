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

class DynamicProxyTest {


    public static void main(String[] args) {
        test();
    }

    private static void test() {
        //委托类(被代理类)
        HelloImpl helloImpl = new HelloImpl();
        IHello proxy = (IHello) Proxy.newProxyInstance(HelloImpl.class.getClassLoader(), HelloImpl.class.getInterfaces(), new InvocationHandler() {
            @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("before method invoke!");
                Object returnValue = method.invoke(helloImpl, args);
                System.out.println("after method invoke!");
                return returnValue;
            }
        });
        proxy.sayHi();
        proxy.sayHello();
        System.out.println("Proxy SimpleName: " + proxy.getClass().getSimpleName());
    }

    private static void test1() {
        //委托类(被代理类)
        HelloImpl helloImpl = new HelloImpl();
        //代理连接器
        ProxyHandler proxyHandler = new ProxyHandler();
        //进一步封装，返回代理对象
        IHello hello = (IHello) proxyHandler.bind(helloImpl);
        hello.sayHello();
        hello.sayHi();
        //命名方式固定，以$开头，proxy为中，最后一个数字表示对象的标号。
        System.out.println(hello.getClass().getName());//com.example.$Proxy0

        // save proxy class to root of this project, you can use jd-gui to see content of the saved file 
        String saveFileName = "$Proxy0.class";
        ProxyUtil.saveProxyClass(saveFileName, helloImpl.getClass().getSimpleName(), helloImpl.getClass().getInterfaces());
    }

    /*
    动态代理要求委托类(被代理类)必须实现了某个接口，约定委托类和代理类之间的协议，代理类才能通过多态调用委托类的方法。
     */
    private static class HelloImpl implements IHello {
        @Override public void sayHello() {
            System.out.println("hello!");
        }

        @Override public void sayHi() {
            System.out.println("hi!");
        }
    }

    private static class ProxyHandler implements InvocationHandler {
        //这个就是我们要代理的真实对象
        private Object originalObj;

        //给我们要代理的真实对象赋初值
        private Object bind(Object originalObj) {
            this.originalObj = originalObj;
            //loader: ClassLoader对象，定义了由哪个ClassLoader对象来对生成的代理对象进行加载
            //interfaces: Interface对象的数组，表示要给代理对象提供什么接口
            //invocationHandler: InvocationHandler对象，当动态生成的动态代理对象在调用方法的时候，会关联到某个InvocationHandler对象上
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
}
