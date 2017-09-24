package com.example.generictypes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/9/25 0025.
 */

public class Test1 {
    public static void main(String[] args) {
        sample3();
    }

    /* 运行报错：虽然编译时obj是一个Object[]，但是在运行时它是一个String[]，它不允许被用于存放一个Integer。 */
    private static void sample1() {
        String[] words = new String[10];
        Object[] obj = words;
        obj[0] = 1;
    }

    /*
    List list1 = new ArrayList()、List<Object> list2 = new ArrayList()、List<?> list2 = new ArrayList()的区别
    List<？>既不是List<Object>也不是一个未经处理的List。
    一个使用通配符的List<?>有两个重要的特性。
    第一，考察类似于get()的方法，他们被声明返回一个值，这个值的类型是类型参数中指定的。
    在这个例子中，类型是“unknown”，所以这些方法返回一个Object。既然我们期望的是调用这个object的toString()方法，程序能够很好的满足我们的意愿。
    第二，考察List的类似add()的方法，他们被声明为接受一个参数，这个参数被类型参数所定义。
    出人意料的是，当类型参数是未确定的，编译器不允许您调用任何有不确定参数类型的方法——因为它不能确认您传入了一个恰当的值。
    一个List(?)实际上是只读的——既然编译器不允许我们调用类似于add(),set(),addAll()这类的方法。
     */
    private void sample2() {
        List list1 = new ArrayList();// 可以编译通过
        list1.add("111");
        list1.add(111);

        List<Object> list2 = new ArrayList();// 可以编译通过
        list2.add("111");
        list2.add(111);

        //        List<?> list2 = new ArrayList();// 不能编译通过
        //        list2.add("111");
        //        list2.add(111);
    }

    /*
     because all instances of a generic class have the same run-time class, regardless of their actual type parameters.
     */
    private static void sample3() {
        List <String> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        System.out.println(l1.getClass() == l2.getClass());// => true
    }

    /* http://docs.oracle.com/javase/tutorial/extra/generics/fineprint.html
     * 泛型数组:
     * 如果下面的代码被允许，那么运行时的数组存储检查将会成功：没有编译时的类型参数，
     * 代码简单地存储一个ArrayList到一个ArrayList[]数组，非常正确。
     * 既然编译器不能阻止您通过这个方法来战胜类型安全，那么它转而阻止您创建一个参数化类型的数组。
     * 所以上述情节永远不会发生，编译器在第一行就开始拒绝编译了。
     * 注意这并不是一个在使用数组时使用泛型的全部的约束，这仅仅是一个创建一个参数化类型数组的约束。
     */
    private void genericArray() {
        //        List<String>[] wordlists = new ArrayList<String>[10];
        //        ArrayList<Integer> ali = new ArrayList<Integer>();
        //        ali.add(123);
        //        Object[] objs = wordlists;
        //        objs[0] = ali;                       // No ArrayStoreException
        //        String s = wordlists[0].get(0);      // ClassCastException!

//        List<String>[] ls = new ArrayList<String>[10];//报错
//        List<String>[] ls2 = new ArrayList[10];//不报错
    }

    public static void printList(List list) {
        for (int i = 0, n = list.size(); i < n; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(list.get(i).toString());
        }
    }

    /* 通配符“？”表示一个未知类型，类型List<?>被读作“List of unknown”  */
    public static void printList2(List<?> list) {
        for (int i = 0, n = list.size(); i < n; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(list.get(i).toString());
        }
    }
}
