package com.king.applib.model;

import java.io.Serializable;

/**
 * @author VanceKing
 * @since 2017/5/23.
 */

public class Student implements Serializable{
    public String name;
    public int age;

    public Student() {
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
