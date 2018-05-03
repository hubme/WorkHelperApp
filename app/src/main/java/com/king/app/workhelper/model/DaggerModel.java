package com.king.app.workhelper.model;

/**
 * Inject ： 注入，被注解的构造方法会自动编译生成一个Factory工厂类提供该类对象。
 *
 * @author VanceKing
 * @since 2018/4/26.
 */

public class DaggerModel {
    private String name;

    public DaggerModel() {

    }

    public DaggerModel(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "DaggerModel{" +
                "name='" + name + '\'' +
                '}' + super.toString();
    }
}
