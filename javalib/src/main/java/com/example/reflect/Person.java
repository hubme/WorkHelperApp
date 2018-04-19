package com.example.reflect;

/**
 * @author VanceKing
 * @since 2018/4/19.
 */
class Person extends Animal {
    private String name;
    private int age;
    private boolean isMale;
    String nickName;
    public String englishName;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, int age, boolean isMale) {
        this.name = name;
        this.age = age;
        this.isMale = isMale;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isMale() {
        return isMale;
    }


    String getNickName() {
        return nickName;
    }


    private String getEnglishName() {
        return englishName;
    }

}
