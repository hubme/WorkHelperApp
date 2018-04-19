package com.example.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author VanceKing
 * @since 2018/4/19 0019.
 */
public class ReflectUsage {
    private static final String DIVIDER = "===================================";

    public static void main(String[] args) throws Exception {
        test1();
    }

    private static void test1() throws Exception {
        final Class<Person> pClass = Person.class;

        printMessage("getFields() 获取类(包括父类)的 public 属性:");
        Field[] fields = pClass.getFields();
        for (Field field : fields) {
            System.out.println(field.toString());
        }
        printDivider();

        printMessage("getDeclaredFields() 获取类的所有属性，但是不包含继承的属性:");
        Field[] declaredFields = pClass.getDeclaredFields();
        for (Field field : declaredFields) {
            System.out.println(field.toString());
        }
        printDivider();

        printMessage("public属性可以用 getDeclaredField()或getField()，其他必须用getDeclaredField()");
        System.out.println(pClass.getField("englishName").toString());
        System.out.println(pClass.getDeclaredField("name").toString());
        System.out.println(pClass.getDeclaredField("nickName").toString());
        printDivider();


        printMessage("getMethods() 获取public方法(包括父类中的):");
        Method[] methods = pClass.getMethods();
        for (Method method : methods) {
            System.out.println(method.toString());
        }
        printDivider();

        printMessage("getDeclaredMethods() 获取当前类中所有方法，不包含继承的:");
        Method[] declaredMethods = pClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            System.out.println(method.toString());
        }
        printDivider();


    }

    private static void printDivider() {
        System.out.println(DIVIDER);
    }

    private static void printMessage(String message) {
        System.out.println(message);
    }
}
