package com.king.app.workhelper.thirdparty.dragger2;

/**
 * @author VanceKing
 * @since 2020/2/27.
 */
public class Milk {
    String type;

    public Milk() {
        type = "";
    }

    public Milk(String shuiguo) {
        type = shuiguo;
    }

    public void addMilk() {
        System.out.println("添加:" + type + "牛奶");
    }
}
