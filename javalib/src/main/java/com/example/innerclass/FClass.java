package com.example.innerclass;

/**
 * 内部类和嵌套类初始化的区别
 *
 * @author VanceKing
 * @since 2017/5/29 0029.
 */

public class FClass {
    int a = 1;

    public class A {
        public void Show() {
            System.out.print("a:" + a);
        }
    }

    public static class B {
        public void Show(FClass h) {
            System.out.print("a:" + h.a);
        }
    }

    /*
    A 因为有一个隐藏的引用，所以必须是H 的实例才能进行初始化出A 类；
    而B 类则是因为是在H 类中以静态方式存在的类，所以需要 new H.B()；
    之所以能直接使用new B()，与该 main 方法在 H 类中有关，
    因为本来就在 H类中，所以直接使用 H类的静态属性或者方法可以不加上：“H.” 在前面。
     */
    public static void main(String[] args) {
        FClass h = new FClass();
//        A a = new A();
        A a1 = h.new A();
        B b = new B();
//        B b1 = h.new B();
        B b3 = new FClass.B();
    }

}
