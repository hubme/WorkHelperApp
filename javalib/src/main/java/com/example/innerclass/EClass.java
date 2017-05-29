package com.example.innerclass;

/**
 * 静态内部类访问权限.
 * 静态嵌套类 A 对其包含类 EClass 完全透明；但 EClass 并不对 A 透明。
 *
 * @author VanceKing
 * @since 2017/5/29 0029.
 */

public class EClass {
    int a1 = 0;
    private int a2 = 0;
    protected int a3 = 0;
    public int a4 = 0;

    //外部类可以访问静态内部类
    private void show() {
        A a = new A();
        System.out.print("b1:" + a.b1);
        System.out.print("b2:" + a.b2);
        System.out.print("b3:" + a.b3);
        System.out.print("b4:" + a.b4);
    }

    //准确的说应该叫做静态嵌套类
    private static class A {
        int b1 = 0;
        private int b2 = 0;
        protected int b3 = 0;
        public int b4 = 0;

        //静态内部类无法访问外部类
        private void print() {
            /*System.out.print("a1:" + a1);
            System.out.print("a2:" + a2);
            System.out.print("a3:" + a3);
            System.out.print("a4:" + a4);*/
        }
    }
}
