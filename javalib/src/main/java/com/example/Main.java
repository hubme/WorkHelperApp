package com.example;

public class Main {
    public static void main(String[] args) {
        Object object = new Main();

//        ((Main) object).getClass().newInstance().print();

        Class cls = ((Main) object).getClass();
        Class<? extends Main> c = ((Main) object).getClass();
//        c.newInstance().print();

        Main main = new Main();
        main.testAuto();
    }

    public void print() {
        System.out.println("哈哈哈");
    }

    /**
     * 为了节省装箱产生对象消耗内存的问题，加了缓存功能。使用了享元模式(flyweight)。
     * 享元模式:运行共享技术有效地支持大量细粒度对象的复用。
     * 内部状态：在享元对象内部不随外界环境改变而改变的共享部分。外部状态：随着环境的改变而改变，不能够共享的状态就是外部状态。
     * https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1.7
     * If the value p being boxed is an integer literal of type int between -128 and 127 inclusive (§3.10.1),
     * or the boolean literal true or false (§3.10.3),
     * or a character literal between '\u0000' and '\u007f' inclusive (§3.10.4),
     * then let a and b be the results of any two boxing conversions of p. It is always the case that a == b.
     */
    public void testAuto() {
        Integer int1 = Integer.valueOf(127);
        Integer int2 = Integer.valueOf(127);
        System.out.println(int1 == int2);

        Integer int3 = Integer.valueOf(128);
        Integer int4 = Integer.valueOf(128);
        System.out.println(int3 == int4);

        String str1 = new String("abc");
        String str2 = new String("abc");
        System.out.printf((str1 == str2)+"");
    }
}
