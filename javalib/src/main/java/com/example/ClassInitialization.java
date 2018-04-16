package com.example;

/**
 * @author VanceKing
 * @since 2018/3/27.
 */

public class ClassInitialization {


    public static void main(String[] args) {
//        ClassInitialization main = new ClassInitialization();
//        main.print();


        System.out.println(A.COUNT);

    }

    private void print() {
        System.out.println("静态初始和实例初始的区别");
    }

    private static class A {

        //同静态代码块 static{ COUNT = 6; }
//        public static int COUNT = 6;
        
        //静态初始化器：加载类时运行的的代码
        //多个static块，按先后顺序执行
        static {
            COUNT = 9;
            System.out.println("静态初始");
        }

        //同静态代码块 static{ COUNT = 6; }
        public static int COUNT = 6;

        static {
            COUNT /= 3;
        }
    }
}
