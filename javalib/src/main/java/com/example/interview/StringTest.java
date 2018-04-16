package com.example.interview;

/**
 * @author VanceKing
 * @since 2018/4/16.
 */

public class StringTest {
    public static void main(String[] args) {

    }

    private static void test1() {
        String str = "hello world!";
        str += "a";
        str += 'a';
        str += " a";//但是不能写成 str += ' a'(a 前面有个空格)，否则编译错误
    }
}
