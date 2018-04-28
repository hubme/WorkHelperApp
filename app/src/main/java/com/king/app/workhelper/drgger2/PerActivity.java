package com.king.app.workhelper.drgger2;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 自定义局部生命周期的注解，使用元注解 Scope 声明。
 *
 * @author VanceKing
 * @since 2018/4/27.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
