package com.example;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.testAuto();
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
    private void testAuto() {
        //[-128, 127] 会缓存。        
        Integer int1 = -128, int2 = -128;
        Integer int3 = -129, int4 = -129;
        System.out.println(Boolean.toString(int1 == int2) + " " + Boolean.toString(int3 == int4));//true false

        Integer int5 = 127, int6 = 127;
        Integer int7 = 1289, int8 = 128;
        System.out.println(Boolean.toString(int5 == int6) + " " + Boolean.toString(int7 == int8));//true false

        String str1 = new String("abc");
        String str2 = new String("abc");
        String str3 = "abc";
        String str4 = "abc";
        //false true false
        System.out.printf(Boolean.toString(str1 == str2) + " " + Boolean.toString(str3 == str4) + " " + Boolean.toString(str1 == str4));
    }
}
