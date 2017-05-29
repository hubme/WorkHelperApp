package com.example.innerclass;

/**
 * 内部类的访问权限.内部类访问外部类(完全访问,private失效).
 *
 * @author VanceKing
 * @since 2017/5/29 0029.
 */

public class CClass {
    int a = 1;
    private int b = 2;
    protected int c = 3;
    public int d = 4;

    void a() {
        System.out.println("A:" + a);
    }

    private void b() {
        System.out.println("B:" + b);
    }

    protected void c() {
        System.out.println("C:" + c);
    }

    public void d() {
        System.out.println("D:" + d);
    }

    class D {
        private int aaa = 1024;
        void show() {
            int max = a + b + c + d;
            a();
            b();
            c();
            d();
            System.out.println("Max:" + max);
        }
    }

    public static void main(String[] args) {
        D d = new CClass().new D();
        d.show();
        System.out.println(d.aaa);
    }
}
