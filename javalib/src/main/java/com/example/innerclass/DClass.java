package com.example.innerclass;

/**
 * 内部类的访问权限.外部类访问内部类(完全访问,private失效).
 *
 * @author VanceKing
 * @since 2017/5/29 0029.
 */

public class DClass {
    class D {
        private int a = 20;

        private void a() {
            System.out.println("D.A:" + a);
        }
    }

    void show() {
        D d = new D();
        d.a();
        System.out.println("D.A:" + d.a);
    }

    public static void main(String[] args) {
        new DClass().show();
    }
}
