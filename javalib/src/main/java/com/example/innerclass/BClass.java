package com.example.innerclass;

/**
 * @author VanceKing
 * @since 2017/5/28 0028.
 */
/*
内部类存在的意义:
主要的就是封装、继承、多态.
内部类的出现就是为了简化多重继承的问题；
一个A类，并不能继承多个其他类，但是在使用中又需要使用到其他类的方法，
这个时候内部类就发挥作用了；典型的就是事件点击的回调实现。
 */
public class BClass {
    public void show() {
        class AUser implements AInterface{
            @Override public void show() {
                System.out.println("AUser");
            }
        }

        AUser aUser = new AUser();
        aUser.show();
    }

    public static void main(String[] args) {
        BClass bClass = new BClass();
        bClass.show();
    }
}
