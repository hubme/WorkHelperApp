package com.example;

/**
 * @author VanceKing
 * @since 2018/3/27.
 */

public class ClassInitialization {
    
    //静态初始化器：加载类时运行的的代码
    static {
        System.out.println("静态初始");
    }

    //实例初始化器：创建新对象时运行的代码
    {
        System.out.println("实例初始");
    }

    public static void main(String[] args) {
        ClassInitialization main = new ClassInitialization();
        main.print();
    }

    private void print() {
        System.out.println("静态初始和实例初始的区别");
    }
}
