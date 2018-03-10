package com.king.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被此注解修饰的类、方法、构造函数、属性不被混淆.
 * 需要在 Proguard 中配置.
 *
 * @author VanceKing
 * @since 2017/2/7.
 */

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface DoNotProguard {
}
