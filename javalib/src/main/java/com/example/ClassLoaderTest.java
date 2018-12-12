package com.example;

import java.io.InputStream;

/**
 * Java类加载器的作用就是在运行时加载类。
 * Java类加载器基于三个机制：委托、可见性和单一性。
 * 委托：加载类的请求先交给父ClassLoader，如果父ClassLoader找不到或不能加载类再交给子ClassLoader加载。
 * 可见性：子ClassLoader可以看见所有的父ClassLoader加载的类，而父ClassLoader看不到子ClassLoader加载的类。
 * 单一性：一个类仅加载一次，这是由委托机制确保子ClassLoader不会再次加载父ClassLoader加载过的类。
 * 三种类加载器加载类文件的地方：
 * 1) Bootstrap类加载器 – JRE/lib/rt.jar
 * 2) Extension类加载器 – JRE/lib/ext或者java.ext.dirs指向的目录
 * 3) Application类加载器 – CLASSPATH环境变量, 由-classpath或-cp选项定义,或者是JAR中的Manifest的classpath属性定义.
 * Created by VanceKing on 2016/12/31 0031.
 */

class ClassLoaderTest {
    public static void main(String[] args) throws Exception {
        test3();
    }

    private static void test3() throws Exception {
        Class<?> clazz = Class.forName("[I");
        System.out.println(clazz);

        //不能加载原生类型。java.lang.ClassNotFoundException: [I
        Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass("[I");
        System.out.println(aClass);
    }

    private static void test2() throws Exception {
        ClassLoader clazzLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return super.loadClass(name);
                /*try {
                    String clazzName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(clazzName);
                    if (is == null) {//为 null 说明加载过。
                        return super.loadClass(name);
                    }
                    System.out.println(name);
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    is.close();
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }*/
            }

            @Override protected Class<?> findClass(String name) throws ClassNotFoundException {
                System.out.println("Class Name: " + name);
                try {
                    String clazzName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(clazzName);
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    is.close();
                    return defineClass(name, b, 0, b.length);
                } catch (Exception e) {
                    throw new ClassNotFoundException(name);
                }

            }
        };

        String currentClass = "com.example.ClassLoaderTest";
        Class<?> clazz = clazzLoader.loadClass(currentClass);
        Object obj = clazz.newInstance();

        System.out.println(obj.getClass());
        //类的全路径相同而且被同一个类加载器加载才是同一个实例。
        System.out.println("是同一个实例吗：" + (obj instanceof ClassLoaderTest));

    }

    private static void test1() {
        System.out.println("ClassLoaderTest.class.getClassLoader(): " + ClassLoaderTest.class.getClassLoader());
        try {
            Class.forName("com.example.ClassLoaderTest", true, ClassLoaderTest.class.getClassLoader().getParent());
        } catch (ClassNotFoundException e) {//单一性机制导致异常。
            e.printStackTrace();
        }
    }
}
