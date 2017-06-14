package com.example.dynamicproxy;

/**
 * 动态代理要求委托类(被代理类)必须实现了某个接口
 *
 * @author huoguangxu
 * @since 2017/6/14.
 */

public class HelloImpl implements IHello {
    @Override public void sayHello() {
        System.out.println("hello world!");
    }
}
