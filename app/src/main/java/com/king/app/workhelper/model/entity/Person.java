package com.king.app.workhelper.model.entity;

import java.io.Serializable;

public class Person implements Serializable{
    public String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}
