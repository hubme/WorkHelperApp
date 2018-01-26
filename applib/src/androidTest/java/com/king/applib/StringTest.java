package com.king.applib;

import com.king.applib.log.Logger;

import org.junit.Test;

/**
 * @author VanceKing
 * @since 2018/1/23.
 */

public class StringTest extends LibBaseAndroidJUnit4Test {

    /*
    (1)先定义一个名为str的对String类的对象引用变量：String str；
    (2)在栈中查找有没有存放值为"abc"的地址，如果没有，则开辟一个存放字面值为"abc"的地址，接着创建一个新的String类的对象o，并将o 的字符串值指向这个地址，而且在栈中这个地址旁边记下这个引用的对象o。如果已经有了值为"abc"的地址，则查找对象o，并返回o的地址。
    (3)将str指向对象o的地址。 值得注意的是，一般String类中字符串值都是直接存值的。但像String str = "abc"；这种场合下，其字符串值却是保存了一个指向存在栈中数据的引用！ 
     */
    @Test
    public void testString() {
        //准确的说法是，创建了一个指向String类的对象的引用变量str1,这个对象引用变量指向了某个值为"abc"的String类。
        String str1 = "abc";
        String str2 = "abc";
        Logger.i(Boolean.toString(str1 == str2));// true
    }

    @Test
    public void testString2() {
        String str1 = "abc";
        String str2 = "abc";
        str1 = "bcd";
        Logger.i(str1 + "," + str2);// bcd,abc
        Logger.i(Boolean.toString(str1 == str2));// false

    }

    /*
    只要是用new()来新建对象的，都会在堆中创建，而且其字符串是单独存值的，即使与栈中的数据相同，也不会与栈中的数据共享。
     */
    @Test
    public void testString3() {
        //创建了两个引用。创建了两个对象。两个引用分别指向不同的两个对象。
        String str1 = new String("abc");//在堆中创建
        String str2 = "abc";//在栈中创建
        Logger.i(Boolean.toString(str1 == str2));// false
    }
}
