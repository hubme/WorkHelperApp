package com.example.entity;

/**
 * @author huoguangxu
 * @since 2017/3/31.
 */

public class Bird {
    public String name;

    public Bird() {
    }

    public Bird(String name) {
        this.name = name;
    }

    @Override public int hashCode() {
        return 1;
    }

    @Override public boolean equals(Object o) {
        return true;
    }

    @Override public String toString() {
        return "bird{" +
                "name='" + name + '\'' +
                '}';
    }
}
