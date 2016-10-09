package com.example;

public class Main {
    public static void main(String[] args) {
        Object object = new Main();

//        ((Main) object).getClass().newInstance().print();

        Class cls = ((Main) object).getClass();
        Class<? extends Main> c = ((Main) object).getClass();
//        c.newInstance().print();
    }

    public void print() {
        System.out.println("哈哈哈");
    }
}
