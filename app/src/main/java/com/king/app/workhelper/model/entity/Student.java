package com.king.app.workhelper.model.entity;

import java.io.Serializable;

/**
 * Created by HuoGuangxu on 2016/9/30.
 */

public class Student implements Serializable {
    public String name;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
