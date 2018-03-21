package com.example.entity;

import java.util.Objects;

/**
 * @author VanceKing
 * @since 2018/3/21.
 */

public class Person {
    private String name;
    private int age;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (o == null || !(o instanceof Person)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        Person person = (Person) o;
        if (this.age == person.getAge() && this.name != null && this.name.equals(person.getName())) {
            return true;
        }
        return false;
    }

    /* 重写了equals()方法，一定要重写hashCode() */
    /*@Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + age;
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }*/

    @Override public int hashCode() {
        return Objects.hash(name, age);
    }
}
