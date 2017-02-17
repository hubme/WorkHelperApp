package com.king.app.workhelper;

import com.king.applib.annotation.AnnotationTest;
import com.king.app.workhelper.model.ReflectTestBean;
import com.king.applib.log.Logger;
import com.king.applib.util.Reflect;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author VanceKing
 * @since 2017/1/19 0019.
 */

public class ReflectTest extends BaseTestCase {
    private Class<?> mClazz;
    @AnnotationTest(date = "2017-02-15 23:03")
    private String testAnnotation;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mClazz = Class.forName("com.king.app.workhelper.model.ReflectTestBean");
    }

    public void testAnnotation() throws Exception {
        Class clazz = Class.forName("com.king.app.workhelper.ReflectTest");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            AnnotationTest annotation = field.getAnnotation(AnnotationTest.class);
            if (annotation != null) {
                Logger.i(annotation.author());
                Logger.i(annotation.date());
                Logger.i(annotation.version() + "");
            }
        }
    }

    /** 通过Java反射机制得到类的包名、类名等信息 */
    public void testDemo1() throws Exception {
        Logger.i("PackageName: " + ReflectTestBean.class.getPackage().getName());//com.king.app.workhelper.model
        Logger.i("getCanonicalName: " + ReflectTestBean.class.getCanonicalName());//com.king.app.workhelper.model.ReflectTestBean
        Logger.i("getName: " + ReflectTestBean.class.getName());//com.king.app.workhelper.model.ReflectTestBean
        Logger.i("getSimpleName: " + ReflectTestBean.class.getSimpleName());//ReflectTestBean
        Logger.i("getGenericSuperclass: " + ReflectTestBean.class.getGenericSuperclass());//class java.lang.Object
        Logger.i("getSuperclass: " + ReflectTestBean.class.getSuperclass());//class java.lang.Object
    }

    /** 验证所有的类都是Class类的实例对象 */
    public void testDemo2() throws Exception {
        Class<?> class1;
        Class<?> class2;

        //写法1, 可能抛出 ClassNotFoundException [多用这个写法]
        class1 = Class.forName("com.king.app.workhelper.model.ReflectTestBean");
        Logger.i("包名: " + class1.getPackage().getName() + "; 类名：" + class1.getName());

        //写法2
        class2 = ReflectTestBean.class;
        Logger.i("包名: " + class2.getPackage().getName() + "; 类名：" + class2.getName());
    }

    /** 通过Java反射机制，用Class 创建类对象[这也就是反射存在的意义所在] */
    public void testDemo3() throws Exception {
        Class<?> clazz = Class.forName("com.king.app.workhelper.model.ReflectTestBean");
        ReflectTestBean model = (ReflectTestBean) clazz.newInstance();
        model.setName("哈哈哈");
        Logger.i(model.getName());
    }

    /***
     * 通过Java反射机制得到一个类的构造函数，并实现创建带参实例对象
     */
    public void testDome4() throws Exception {
        Class<?> clazz = Class.forName("com.king.app.workhelper.model.ReflectTestBean");
        Constructor<?>[] constructors = clazz.getConstructors();
        ReflectTestBean model1 = (ReflectTestBean) constructors[0].newInstance();
        model1.setName("007");
        Logger.i(model1.getName());

        ReflectTestBean model2 = (ReflectTestBean) constructors[1].newInstance("008");
        Logger.i(model2.getName());
    }

    /**
     * 通过Java反射机制操作成员变量
     */
    public void testDemo5() throws Exception {
        Object object = mClazz.newInstance();

        Field nameField = mClazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(object, "哈哈哈");

        Logger.i(nameField.get(object).toString());
    }

    /**
     * 通过Java反射机制得到类的一些属性： 继承的接口，父类，函数信息，成员信息，类型等
     */
    public void testDemo6() throws Exception {
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            Logger.i(field.getName());
        }
    }

    /**
     * 通过Java反射机制调用类方法
     */
    public void testDemo7() throws Exception {
        //调用无参方法
        Method method = mClazz.getMethod("print");//方法权限定义为private报错，找不到方法。
        method.setAccessible(true);
        method.invoke(mClazz.newInstance());

        //调用有参方法
        Method method1 = mClazz.getMethod("print", String.class);
        method1.setAccessible(true);
        method1.invoke(mClazz.newInstance(), "VanceKing");
    }

    /**
     * 通过反射机制得到类加载器信息
     * 1）Bootstrap ClassLoader 此加载器采用c++编写，一般开发中很少见。
     * 2）Extension ClassLoader 用来进行扩展类的加载，一般对应的是jre\lib\ext目录中的类
     * 3）AppClassLoader 加载classpath指定的类，是最常用的加载器。同时也是java中默认的加载器。
     */
    public void testDemo8() throws Exception {
        Logger.i("类加载器类名: " + mClazz.getClassLoader().getClass().getName());
    }

    public void testDemo9() throws Exception {
        String world = Reflect.on("java.lang.String")  // Like Class.forName()
                .create("Hello World") // Call most specific matching constructor
                .call("substring", 6)  // Call most specific matching substring() method
                .call("toString")      // Call toString()
                .get();                // Get the wrapped object, in this case a String
        Logger.i("world: " + world);

        char pathSeparatorChar = Reflect.on(File.class).create("/sdcard/droidyue.com").field("pathSeparatorChar").get();
        Logger.i("pathSeparatorChar: " + pathSeparatorChar);

        ArrayList arrayList = new ArrayList();
        arrayList.add("Hello");
        arrayList.add("World");
        int value = Reflect.on(arrayList).call("hugeCapacity", 12).get();
        Logger.i("value: " + value);
    }
}
