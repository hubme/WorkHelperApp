package com.example.generictypes;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

/**
 * @author VanceKing
 * @since 2018/4/12.
 */
public class TypeSample {
    private Map<Integer, String> map;
    private List<? extends Number> a;
    private List<? super String> b;

    public static void main(String[] args) throws Exception {
        testWildcardType();
    }

    private static void testParameterizedType() throws Exception {
        Field field = TypeSample.class.getDeclaredField("map");
        System.out.println(field.getGenericType());//java.util.Map<java.lang.Integer, java.lang.String>
        System.out.println(field.getGenericType() instanceof ParameterizedType);//true
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        System.out.println(genericType.getRawType());//interface java.util.Map
        for (Type type : genericType.getActualTypeArguments()) {
            System.out.println(type);//class java.lang.Integer class java.lang.String
        }
        System.out.println(genericType.getOwnerType());//null
    }

    private static void testTypeVariable() throws Exception {
        Field keyField = TypeVariableBean.class.getDeclaredField("key");
        Field valueField = TypeVariableBean.class.getDeclaredField("value");

        TypeVariable keyVariable = (TypeVariable) keyField.getGenericType();
        TypeVariable valueVariable = (TypeVariable) valueField.getGenericType();

        System.out.println("keyVariable.getName(): " + keyVariable.getName());//K
        System.out.println("valueVariable.getName(): " + valueVariable.getName());//V

        System.out.println("keyVariable.getGenericDeclaration(): " + keyVariable.getGenericDeclaration());
        System.out.println("valueVariable.getGenericDeclaration(): " + valueVariable.getGenericDeclaration());

        //只能获取上边界
        Type[] keyBounds = keyVariable.getBounds();
        for (Type type : keyBounds) {
            System.out.println(type.getTypeName());

        }
        Type[] valueBounds = valueVariable.getBounds();
        for (Type type : valueBounds) {
            System.out.println(type.getTypeName());
        }
    }

    private static void testGenericArrayType() {
        Method showMethod = TypeSampleBean.class.getDeclaredMethods()[0];
        Type[] parameterTypes = showMethod.getGenericParameterTypes();
        for (Type type : parameterTypes) {
            boolean b = (type instanceof GenericArrayType);
            //true, true, false, false, false
            System.out.println(type.getTypeName() + "是 GenericArrayType 类型吗？ " + b);
            if (b) {
                GenericArrayType arrayType = (GenericArrayType) type;
                Type componentType = arrayType.getGenericComponentType();//返回数组的组成对象
                System.out.println("数组的组成对象是：" + componentType.getTypeName());//java.util.List<java.lang.String> T 
                System.out.println();
            }
        }
    }

    /*
    eg: List<? extends Number>, 上界为class java.lang.Number, 属于Class类型
        List<? extends List<T>>, 上界为java.util.List<T>, 属于ParameterizedType类型
        List<? extends List<String>>, 上界为java.util.List<java.lang.String>,属于ParameterizedType类型
        List<? extends T>, 上界为T, 属于TypeVariable类型
        List<? extends T[]>, 上界为T[], 属于GenericArrayType类型
     */
    private static void testWildcardType() throws Exception {
        Field aField = TypeSample.class.getDeclaredField("a");
        Field bField = TypeSample.class.getDeclaredField("b");

        ParameterizedType aParaType = (ParameterizedType) aField.getGenericType();//java.util.List<? extends java.lang.Number>
        ParameterizedType bParaType = (ParameterizedType) bField.getGenericType();//java.util.List<? super java.lang.String>

        WildcardType aWildcardType = (WildcardType) aParaType.getActualTypeArguments()[0];
        WildcardType bWildcardType = (WildcardType) bParaType.getActualTypeArguments()[0];

        Type aFieldUpper = aWildcardType.getUpperBounds()[0];
        System.out.println("List<? extends Number> of getUpperBounds(): " + aFieldUpper);

        Type bFieldLower = bWildcardType.getLowerBounds()[0];
        System.out.println("List<? super String> of getLowerBounds(): " + bFieldLower);
    }

    private static class TypeVariableBean<K extends Comparable & Serializable, V> {
        private K key;
        private V value;
    }

    private static class TypeSampleBean<T> {
        public void show(List<String>[] p1, T[] p2, List<Integer> p3, String[] p4, int[] p5) {

        }
    }
}
