package com.example.dynamicproxy;

/**
 * 静态代理示例。
 */
class StaticProxy {
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        //创建被代理对象或委托类
        RealHello realHello = new RealHello();
        //创建代理类
        ProxyHello proxyHello = new ProxyHello(realHello);
        //通过代理对象调用委托对象的方法。
        proxyHello.sayHello();
        proxyHello.sayHi();
    }

    private static class RealHello implements IHello {

        @Override public void sayHello() {
            System.out.println("Real hello!");
        }

        @Override public void sayHi() {
            System.out.println("Real hai!");
        }
    }

    private static class ProxyHello implements IHello {
        private IHello proxy;

        private ProxyHello(IHello proxy) {
            this.proxy = proxy;
        }

        @Override public void sayHello() {
            System.out.println("Proxy, before hello!");
            proxy.sayHello();
        }

        @Override public void sayHi() {
            System.out.println("Proxy, before hi!");
            proxy.sayHi();
        }
    }
}
