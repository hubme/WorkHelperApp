package com.example.reflect;

/**
 * @author VanceKing
 * @since 2018/4/19.
 */
public class Animal {
    private String type;
    public boolean canSpeak;

    public Animal() {
    }

    public Animal(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
