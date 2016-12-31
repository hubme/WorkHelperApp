package com.example.exception;

/**
 * Created by VanceKing on 2016/12/31 0031.
 */

public class ExceptionMain {
    public static void main(String[] args) {
        ExceptionMain main = new ExceptionMain();
        try {
            main.reThrowException("First");
        } catch (FirstException | SecondException e) {//异常参数变量(e)是定义为final的，所以不能被修改
            System.out.print(e.getMessage());
            e.printStackTrace();
        }
    }

    private void reThrowException(String s) throws FirstException, SecondException{
        if ("First".equals(s)) {
            throw new FirstException("First");
        } else if ("Second".equals(s)) {
            throw new SecondException("Second");
        }
    }
}
