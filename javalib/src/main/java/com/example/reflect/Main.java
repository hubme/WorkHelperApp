package com.example.reflect;

import com.example.annotation.MethodInfo;
import com.example.entity.Bird;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author VanceKing
 * @since 2017/6/14.
 */

class Main {
    private static final String CLASS_NAME = "com.example.entity.Bird";

    public static void main(String[] args) throws Exception {
        getAnnotationInfo();
    }

    
    private static void getAnnotationInfo() {
        try {
            Class cls = Class.forName("com.example.reflect.Main");
            for (Method method : cls.getMethods()) {
                MethodInfo methodInfo = method.getAnnotation(MethodInfo.class);
                if (methodInfo != null) {
                    System.out.println("method name:" + method.getName());
                    System.out.println("method author:" + methodInfo.author());
                    System.out.println("method version:" + methodInfo.version());
                    System.out.println("method date:" + methodInfo.date());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @MethodInfo(author = "VanceKing", date = "2019/06/19", version = 2)
    public void methodWithAnnotation() {
        
    }

    private static void test1() {
        Bird bird = new Bird();
        Class<? extends Bird> aClass = bird.getClass();
        System.out.println(aClass.getName());

        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            System.out.println(field.getName());
        }

        Constructor<?>[] constructors = aClass.getConstructors();
        for (Constructor t : constructors) {
            t.setAccessible(true);
            System.out.println(t.getName());
        }
    }

    private static void testClassForName() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> aClass = Class.forName(CLASS_NAME);
        Constructor<?> constructor = aClass.getConstructor(String.class);
        Object obj = constructor.newInstance("bad");
        System.out.println(obj.toString());
    }

    private static void testShowDeclaredMethods() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Bird bird = new Bird("stupid");

        Method[] methods = bird.getClass().getDeclaredMethods();
        printMethodName(methods);

        Method flyMethod = bird.getClass().getDeclaredMethod("fly", String.class);
        flyMethod.setAccessible(true);
        Class<?>[] parameterTypes = flyMethod.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            System.out.println("fly method params type is: " + parameterType.getName());
        }
        System.out.println(flyMethod.getName()+" is private "+ Modifier.isPrivate(flyMethod.getModifiers()));
        flyMethod.invoke(bird, "hahaha");
    }

    private static void testShowMethod() {
        Bird bird = new Bird("stupid");
        //getMethods只能获取公共方法，如果获取私有方法则会抛出异常
        Method[] methods = bird.getClass().getMethods();
        printMethodName(methods);
    }

    private static void printMethodName(Method[] methods) {
        for (Method method : methods) {
            method.setAccessible(true);
            System.out.println(method.getName());
        }
    }
}
