package com.example.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author VanceKing
 * @since 2018/4/21.
 */
public class Sample1 {
    private static class SuperClass {
        private int id;

        public SuperClass() {
        }

        public SuperClass(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class ReflectTestBean extends SuperClass {
        private String name;
        private int age;

        public ReflectTestBean() {
        }

        public ReflectTestBean(String name) {
            this.name = name;
        }

        public ReflectTestBean(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void print() {
            System.out.println("hello!");
        }

        public void print(String text) {
            System.out.println(text);
        }
    }

    public static void main(String[] args) throws Exception {
        Sample1 sample1 = new Sample1();
        sample1.testDemo7();

    }

    /** 通过Java反射机制得到类的包名、类名等信息 */
    public void testDemo1() {
        System.out.println();
        System.out.println("PackageName: " + ReflectTestBean.class.getPackage().getName());//com.example.reflect
        System.out.println("getCanonicalName: " + ReflectTestBean.class.getCanonicalName());//com.example.reflect.Sample1.ReflectTestBean
        System.out.println("getName: " + ReflectTestBean.class.getName());//com.example.reflect.Sample1$ReflectTestBean
        System.out.println("getSimpleName: " + ReflectTestBean.class.getSimpleName());//ReflectTestBean
        System.out.println("getGenericSuperclass: " + ReflectTestBean.class.getGenericSuperclass());//class com.example.reflect.Sample1$SuperClass
        System.out.println("getSuperclass: " + ReflectTestBean.class.getSuperclass());//class com.example.reflect.Sample1$SuperClass
    }

    /** 验证所有的类都是Class类的实例对象 */
    public void testDemo2() throws Exception {
        //写法1, 可能抛出 ClassNotFoundException [多用这个写法]
        //错误写法：com.example.reflect.Sample1.ReflectTestBean 正确写法：com.example.reflect.Sample1$ReflectTestBean
        Class<?> class1 = Class.forName("com.example.reflect.Sample1$ReflectTestBean");
        System.out.println("包名: " + class1.getPackage().getName() + "; 类名：" + class1.getName());

        //写法2
        Class<?> class2 = ReflectTestBean.class;
        System.out.println("包名: " + class2.getPackage().getName() + "; 类名：" + class2.getName());
    }

    /** 通过Java反射机制，用Class 创建类对象[这也就是反射存在的意义所在] */
    public void testDemo3() throws Exception {
        Class<?> clazz = Class.forName("com.example.reflect.Sample1$ReflectTestBean");
        ReflectTestBean model = (ReflectTestBean) clazz.newInstance();
        model.setName("哈哈哈");
        System.out.println(model.getName());
    }

    /***
     * 通过Java反射机制得到一个类的构造函数，并实现创建带参实例对象
     */
    public void testDemo4() throws Exception {
        Class<?> clazz = Class.forName("com.example.reflect.Sample1$ReflectTestBean");

        //无参构造器
        Constructor<?> constructor1 = clazz.getConstructor();
        ReflectTestBean instance1 = (ReflectTestBean) constructor1.newInstance();
        instance1.setName("aaa");
        System.out.println(instance1.getName());

        //一个参数构造器
        Constructor<?> constructor2 = clazz.getConstructor(String.class);
        ReflectTestBean instance2 = (ReflectTestBean) constructor2.newInstance("bbb");
        System.out.println(instance2.getName());

        //2个参数构造器
        Constructor<?> constructor3 = clazz.getConstructor(String.class, int.class);
        ReflectTestBean instance3 = (ReflectTestBean) constructor3.newInstance("VanceKing", 18);
        System.out.println(instance3.getName() + " " + instance3.getAge());

    }

    /**
     * 通过Java反射机制操作成员变量
     */
    public void testDemo5() throws Exception {
        Class<ReflectTestBean> mClazz = ReflectTestBean.class;
        ReflectTestBean instance = mClazz.newInstance();

        System.out.println(instance.getName());
        Field nameField = mClazz.getDeclaredField("name");
        //private 变量要设置访问性，否则can not access a member of class ... with modifiers "private"
        nameField.setAccessible(true);
        nameField.set(instance, "哈哈哈");

        System.out.println(instance.getName());
    }

    /**
     * 通过Java反射机制得到类的一些属性： 继承的接口，父类，函数信息，成员信息，类型等
     */
    public void testDemo6() throws Exception {
        Class<ReflectTestBean> mClazz = ReflectTestBean.class;
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("field: " + field.getName());
        }
        ReflectTestBean instance = mClazz.newInstance();
        //调用无参方法
        Method method = mClazz.getMethod("print");
        method.setAccessible(true);
        method.invoke(instance);

        //调用有参方法
        Method method1 = mClazz.getMethod("print", String.class);
        method1.setAccessible(true);
        method1.invoke(instance, "VanceKing");
    }

    /**
     * 通过反射机制得到类加载器信息
     */
    public void testDemo7() {
        Class<ReflectTestBean> mClazz = ReflectTestBean.class;
        System.out.println("类加载器: " + mClazz.getClassLoader().getClass().getName());
        System.out.println("AppClassLoader 的父加载器: " + mClazz.getClassLoader().getParent().getClass().getName());
        System.out.println("ExtClassLoader 的父加载器: " + mClazz.getClassLoader().getParent().getParent());
    }

}
