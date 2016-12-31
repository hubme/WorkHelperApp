package com.example;

/**
 * Java类加载器的作用就是在运行时加载类。
 * Java类加载器基于三个机制：委托、可见性和单一性。
 * 委托：加载类的请求先交给父ClassLoader，如果父ClassLoader找不到或不能加载类再交给子ClassLoader加载。
 * 可见性：子ClassLoader可以看见所有的父ClassLoader加载的类，而父ClassLoader看不到子ClassLoader加载的类。
 * 单一性：一个类仅加载一次，这是由委托机制确保子ClassLoader不会再次加载父ClassLoader加载过的类。
 *
 * 三种类加载器加载类文件的地方：
 * 1) Bootstrap类加载器 – JRE/lib/rt.jar
 * 2) Extension类加载器 – JRE/lib/ext或者java.ext.dirs指向的目录
 * 3) Application类加载器 – CLASSPATH环境变量, 由-classpath或-cp选项定义,或者是JAR中的Manifest的classpath属性定义.
 * Created by VanceKing on 2016/12/31 0031.
 */

public class ClassLoaderTest {
    public static void main(String[] args) {
        System.out.println("ClassLoaderTest.class.getClassLoader(): " + ClassLoaderTest.class.getClassLoader());
        try {
            Class.forName("com.example", true, ClassLoaderTest.class.getClassLoader().getParent());
        } catch (ClassNotFoundException e) {//单一性机制导致异常。
            e.printStackTrace();
        }
    }
}
