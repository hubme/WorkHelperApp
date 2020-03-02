package com.king.app.workhelper.thirdparty.dragger2;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * 用户区分类型相同，但是有区别（构造传参不同）的类。
 *
 * @author VanceKing
 * @since 2020/2/27.
 */
@Qualifier//限定符
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
    String value() default "";
}
