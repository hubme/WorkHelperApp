package com.example.innerclass;

/**
 * 内部类的继承
 *
 * @author VanceKing
 * @since 2017/5/29 0029.
 */

public class ChildDClass extends DClass.D {

    /*
    也就是在构造方法中，传递一个 DClass 的引用进去，
    并调用 DClass 实例的 super() 方法，才能进行实例化。
     */
    public ChildDClass(DClass dClass) {
        dClass.super();
    }

    public static void main(String[] args) {
        DClass dClass = new DClass();
        ChildDClass childDClass = new ChildDClass(dClass);
    }
}
