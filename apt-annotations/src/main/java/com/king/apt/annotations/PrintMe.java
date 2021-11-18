package com.king.apt.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author VanceKing
 * @since 2018/10/18.
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.SOURCE)
public @interface PrintMe {
    String value() default "";
}
