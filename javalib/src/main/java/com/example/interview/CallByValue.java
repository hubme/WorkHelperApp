package com.example.interview;

/**
 * @author VanceKing
 * @since 2018/2/2.
 */

public class CallByValue {

    public static void main(String[] args) {
        User user = new User("zhangsan", 26);
        User user2 = new User("VanceKing", 18);
        System.out.println("调用前user的值：" + user.toString());
        System.out.println("调用前user2的值：" + user2.toString());
        swap(user, user2);
        System.out.println("调用后user的值：" + user.toString());
        System.out.println("调用后user2的值：" + user2.toString());
    }

    public static void swap(User x, User y) {
        User temp = x;
        x = y;
        y = temp;
    }
}

class User {
    private String name;
    private int age;

    public User(String name, int age) {
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
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}