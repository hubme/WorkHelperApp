package com.king.applib.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;

/**
 * @author VanceKing
 * @since 2022/1/24
 */
public class AnnotateNaming implements FieldNamingStrategy {
    @Override
    public String translateName(Field field) {
        ParamNames a = field.getAnnotation(ParamNames.class);
        return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
    }
}
