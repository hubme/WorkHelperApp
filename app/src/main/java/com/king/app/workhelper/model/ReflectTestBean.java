package com.king.app.workhelper.model;

import com.king.applib.log.Logger;

/**
 * @author VanceKing
 * @since 2017/1/19 0019.
 */

public class ReflectTestBean {
    private String name;
    private String age;

    public ReflectTestBean() {
    }

    public ReflectTestBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void print() {
        Logger.i("呀呀");
    }

    public void print(String text) {
        Logger.i(text);
    }
}
