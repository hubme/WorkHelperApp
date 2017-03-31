package com.example;

import com.example.entity.Bird;

/**
 * equals和hashCode的理解<br/>
 * http://www.cnblogs.com/skywang12345/p/3324958.html
 *
 * @author huoguangxu
 * @since 2017/3/31.
 */

public class EqualsAndHash {
    public static void main(String[] args) {
        Bird bird1 = new Bird("aaa");
        Bird bird2 = new Bird("aaa");
        Bird bird3 = bird2;

        if (bird1.equals(bird2)) {
            System.out.println("==");
        } else {
            System.out.println("!=");
        }

        /*if (bird3.equals(bird2)) {
            System.out.println("==");
        } else {
            System.out.println("!=");

        } */
    }
}
