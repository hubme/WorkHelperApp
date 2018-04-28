package com.king.app.workhelper.model.dagger2;

/**
 * Inject ： 注入，被注解的构造方法会自动编译生成一个Factory工厂类提供该类对象。
 *
 * @author VanceKing
 * @since 2018/4/26.
 */

public class DaggerModel2 {
    private String name;

    public DaggerModel2() {

    }

    public DaggerModel2(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "DaggerModel{" +
                "name='" + name + '\'' +
                '}' + super.toString();
    }
}
