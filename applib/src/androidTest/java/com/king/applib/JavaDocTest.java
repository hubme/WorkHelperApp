package com.king.applib;

/**
 * 文档注释"@link"的使用
 * Created by HuoGuangxu on 2016/10/20.
 */

public class JavaDocTest {

    private void method1() {

    }

    private void method2(String aaa) {

    }

    /**
     * 1.在当前类，类名可以省略<br/>
     * {@link JavaDocTest#method1()}<br/>
     * {@link #method2(String)}<br/>
     * 2.不在当前类，要加上类的全路径
     * {@link com.king.applib.util.JsonUtil#GSON Gson对象}
     */
    public void test() {

    }
}
