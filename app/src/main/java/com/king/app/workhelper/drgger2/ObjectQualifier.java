package com.king.app.workhelper.drgger2;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * 自定义限定符，供 Dagger2 确定调用哪个方法生成对象依赖，解决依赖迷失的问题。和 @Named 作用相同。
 *
 * @author VanceKing
 * @since 2018/4/27.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectQualifier {
    int value() default 0;
}
