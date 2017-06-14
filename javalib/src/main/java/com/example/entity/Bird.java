package com.example.entity;

/**
 * @author huoguangxu
 * @since 2017/3/31.
 */

public class Bird {
    public String name;

    public int age;

    public Bird() {
    }

    public Bird(String name) {
        this.name = name;
    }

    public Bird(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private void fly(String type) {
        System.out.println("fly method--->" + type);
    }

    public void eat(String food) {
        System.out.println("I can eat " + food);
    }

    @Override public int hashCode() {
        return 1;
    }

    @Override public boolean equals(Object o) {
        return true;
    }

    @Override public String toString() {
        return "I am a " + name + " bird.";
    }
}
