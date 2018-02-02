package com.example.interview;

/**
 * ���� Java ��������ʼ����
 *
 * @author VanceKing
 * @since 2018/2/2.
 */

public class ClassLoadInitTest {
    public static void main(String[] args) {
        System.out.println("--------- new Child()");
        Child child = new Child();
        System.out.println("\n--------- c.action");
        child.action();

        Base b = child;
        System.out.println("\n-------- b.action()");
        b.action();

        System.out.println("\n------- b.s: " + b.s);
        System.out.println("-------- c.s: " + child.s);

        /*
        
        --------- new Child()
        ���ྲ̬�����, s:0
        ���ྲ̬�����, s:0
        ����ʵ�������, a:0
        ���๹�췽��, a:1
        ����ʵ�������, a:0
        ���๹�췽��, a:10
        
        --------- c.action
        start
        child s: 10 , a: 20
        end
        
        -------- b.action()
        start
        child s: 10 , a: 20
        end
        
        ------- b.s: 1
        -------- c.s: 10
        
         */
    }
}

class Base {
    public static int s;
    private int a;

    static {
        System.out.println("���ྲ̬�����, s:" + s);
        s = 1;
    }

    {
        System.out.println("����ʵ�������, a:" + a);
        a = 1;
    }

    public Base() {
        System.out.println("���๹�췽��, a:" + a);
        a = 2;
    }

    protected void setUp() {
        System.out.println("base s: " + s + " , a : " + a);
    }

    public void action() {
        System.out.println("start");
        setUp();
        System.out.println("end");
    }
}

class Child extends Base {
    public static int s;
    private int a;

    static {
        System.out.println("���ྲ̬�����, s:" + s);
        s = 10;
    }

    {
        System.out.println("����ʵ�������, a:" + a);
        a = 10;
    }

    public Child() {
        System.out.println("���๹�췽��, a:" + a);
        a = 20;
    }

    protected void setUp() {
//        super.setUp();
        System.out.println("child s: " + s + " , a: " + a);
    }
}