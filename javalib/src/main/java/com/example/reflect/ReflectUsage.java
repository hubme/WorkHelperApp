package com.example.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author VanceKing
 * @since 2018/4/19 0019.
 */
public class ReflectUsage<T> {
    private static final String DIVIDER = "===================================";
    private List<String>[] array;
    private T[] tArray;

    public static void main(String[] args) throws Exception {
        //test1();

        ReflectUsage<String> aaa = new ReflectUsage<>();
        aaa.aaa();
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


        printMessage("getClassLoader(): " + pClass.getClassLoader().toString() + " getCanonicalName: " + pClass.getCanonicalName() +
                " getName: " + pClass.getName() + " getSimpleName: " + pClass.getSimpleName());
        printDivider();

        Class<? super Person> superclass = pClass.getSuperclass();
        printMessage("getCanonicalName: " + superclass.getCanonicalName());
    }

    private static void printDivider() {
        System.out.println(DIVIDER);
    }

    private static void printMessage(String message) {
        System.out.println(message);
    }

    private void aaa() throws Exception {
        Class<ReflectUsage> clazz = ReflectUsage.class;
        Type array = clazz.getDeclaredField("array").getGenericType();
        // java.util.List<java.lang.String>[]
        System.out.println(array);
        if (array instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) array).getGenericComponentType();
            // java.util.List<java.lang.String>
            System.out.println(genericComponentType);
        }

        Type tArray = clazz.getDeclaredField("tArray").getGenericType();
        // T[]
        System.out.println(tArray);
        if (tArray instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) tArray).getGenericComponentType();
            // T
            System.out.println(genericComponentType);
        }
    }
}
