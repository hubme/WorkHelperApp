package com.king.app.workhelper.model.entity;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable{
    public String name;
    public int age;
    public boolean isMan;
    public String address;
    public String habit;
    public List<Person> children;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", isMan=" + isMan +
                ", address='" + address + '\'' +
                ", habit='" + habit + '\'' +
                ", children=" + children +
                '}';
    }
}
