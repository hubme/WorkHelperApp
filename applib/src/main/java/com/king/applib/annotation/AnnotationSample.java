package com.king.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation is a form of metadata, that can be added to Java source code.
 * Classes, methods, variables, parameters and packages may be annotated.
 * Annotations have no direct effect on the operation of the code they annotate.
 * Retention 保留的范围，默认值为CLASS.SOURCE:只在源码中可用;CLASS:在源码和字节码中可用;RUNTIME:在源码,字节码,运行时均可用
 * Target 可以用来修饰哪些程序元素，如 TYPE, METHOD, CONSTRUCTOR, FIELD, PARAMETER等，未标注则表示可修饰所有
 * Inherited 是否可以被继承，默认为false
 * Documented 是否会保存到 Javadoc 文档中
 * 解析编译时注解需要继承AbstractProcessor类, 实现其抽象方法public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
 *
 * @author VanceKing
 * @since 2017/2/15
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface AnnotationSample {
    String author() default "VanceKing";

    String date();

    int version() default 1;
}
