package com.king.app.workhelper;

import android.util.SparseArray;

import com.king.applib.log.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * @author huoguangxu
 * @since 2017/3/31.
 */

public class ListTest extends BaseTestCase {

    private List<String> stringList;
    private List<String> hasNullList;
    private SparseArray<String> mStringSparseArray;

    @Override protected void setUp() throws Exception {
        super.setUp();
        stringList = new ArrayList<>();
        stringList.add("0");
        stringList.add("1");
        stringList.add("3");

        hasNullList = new ArrayList<>();
        hasNullList.add("4");
//        hasNullList.add(null);
        hasNullList.add("5");

        mStringSparseArray = new SparseArray<>();
    }

    //List是否可以添加null，是否会抛异常。
    public void testList() throws Exception {
        stringList.addAll(hasNullList);//strings1的内容可以为null，但是string1不能为null.
        for (int i = 0, size = stringList.size(); i < size; i++) {
            final String s = stringList.get(i);
            if (s == null) {
                Logger.i("第" + i + "个是null");
            } else {
                Logger.i(i + "===" + s);
            }
        }
    }

    //测试List.addAll()是深拷贝还是浅拷贝.结论：浅复制.
    public void testAddAll() throws Exception {
        List<Person> list1 = new ArrayList<>();
        list1.add(new Person("aaa"));
        list1.add(new Person("bbb"));

        List<Person> list2 = new ArrayList<>();
        Person person111 = new Person("111");
        list2.add(person111);
        list2.add(new Person("222"));
        
        list1.addAll(list2);

        person111.setName("哈哈哈");
        printList(list1);
        printList(list2);
    }

    public void testSArray() throws Exception {
        mStringSparseArray.append(0, "000");
        mStringSparseArray.append(1, "aaa");
        mStringSparseArray.append(1, "bbb");//key相同时，把前面的值给覆盖了

        for (int i = 0, size = mStringSparseArray.size(); i < size; i++) {
            Logger.i("index: " + i + ";results: " + mStringSparseArray.get(mStringSparseArray.keyAt(i)));
        }
    }
    
    public void testListSub() throws Exception {
        List<String> subList = stringList.subList(1, 2);
//        subList.add("aaa");//修改sublist会影响原来的list
//        stringList.add("4");//修改原list,则sublist的所有操作会报错。java.util.ConcurrentModificationException
        stringList.clear();
        printList(stringList);
        printList(subList);
    }
    
    public void testArraysAsList() throws Exception {
        //基本类型是不能作为泛型参数的,所以Arrays.asList()只能接收引用类型，自然为了编译通过编译器就把上面的 int[] 数组当做了一个引用参数
        int[] arrays1 = {1, 2, 3};
        List<int[]> list1 = Arrays.asList(arrays1);
        Logger.i("arrays1 size: " + list1.size());//size == 1
        
        Integer[] arrays2 = {1, 2, 3};
        List<Integer> list2 = Arrays.asList(arrays2);
        Logger.i("arrays2 size: " + list2.size());//size == 3
        //抛出 java.lang.UnsupportedOperationException 异常。因为Arrays.asList返回的是 Arrays 的静态私有内部类实现，不是java.util.ArrayList。有的方法(add、remove等)没有实现。
//        list2.add(4);
//        ArrayList<Integer> integers = Arrays.asList(1, 2, 3);//编译不通过无法转换成。java.util.ArrayList

        ArrayList<String> list3 = new ArrayList<>();
        list3.add("android");
        Vector<String> vector1 = new Vector<>();
        vector1.add("android");
        Logger.i("is equals ? " + list3.equals(vector1));
    }
    
    public void testCollectionsMax() throws Exception {
        List<Integer> data = Arrays.asList(1, 20, 3);
        int aaa = Collections.max(data, new Comparator<Integer>() {
            @Override public int compare(Integer o1, Integer o2) {
                if (o1 < o2) {
                    return -1;
                } else if (o1 > o2) {
                    return 1;
                }
                return 0;
            }
        });
        Logger.i("" + aaa);
    }
    
    private static class Person{
        private String name;

        public Person() {
        }

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
}
