package com.example.generictypes;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author VanceKing
 * @since 2018/4/12.
 */
public class TestType {
    private Map<Integer, String> map;

    public static void main(String[] args) throws NoSuchFieldException {
        Field field = TestType.class.getDeclaredField("map");
        System.out.println(field.getGenericType());//java.util.Map<java.lang.Integer, java.lang.String>
        System.out.println(field.getGenericType() instanceof ParameterizedType);//true
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        System.out.println(genericType.getRawType());//interface java.util.Map
        for (Type type : genericType.getActualTypeArguments()) {
            System.out.println(type);//class java.lang.Integer class java.lang.String
        }
        System.out.println(genericType.getOwnerType());//null
    }
}
