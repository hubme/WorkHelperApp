package com.example.interview;

/**
 * try catch finally 含有 return 语句时执行循序。
 *
 * @author VanceKing
 * @since 2018/2/2.
 */

public class TryCatchReturnTest {
    public static void main(String[] args) {

        /*int result = action1();
        System.out.println("result: " + result);*/

        /*Person result2 = action2();
        System.out.println("result: " + result2);*/

        System.out.println(action3());
    }

    private static int action1() {
        int result = 1;
        try {
            return result;
        } finally {
            ++result;
            return result;
        }
    }

    private static Person action2() {
        Person person = new Person("Hai");
        try {
            return person;
        } finally {
            person.setName("VanceKing");
        }
    }

    private static int action3() {
        try {
            if (true) {
                throw new NullPointerException("");
            }
            return 1;
        } catch (Exception e) {
            if (true) {
                System.exit(0);
            }
            return 2;
        } finally {
            System.out.println("finally");
        }
    }
}

class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}