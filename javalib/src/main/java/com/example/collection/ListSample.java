package com.example.collection;

import com.example.entity.Person;
import com.example.util.Utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author VanceKing
 * @since 2018/3/21.
 */

public class ListSample {

    public static void main(String[] args) {
        testListSub();
    }

    private static void testEqualsHashCode() {
        Person person1 = new Person("aaa", 20);
        Person person2 = new Person("aaa", 20);

        Set<Person> personSet = new HashSet<>();
        personSet.add(person1);
        personSet.add(person2);

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);

        System.out.println("person1 == person2: " + (person1 == person2));
        System.out.println("hashCode of person1: " + person1.hashCode());
        System.out.println("hashCode of person2: " + person2.hashCode());
        System.out.println("person1.equals(person2): " + person1.equals(person2));
    }

    private static void testListCopy() {
        Person person1 = new Person("aaa");
        Person person2 = new Person("bbb");

        ArrayList<Person> personList1 = new ArrayList<>();
        personList1.add(person1);
        personList1.add(person2);

        List<Person> personList2 = (ArrayList<Person>) personList1.clone();
        //ArrayList 基于数组实现。List#remove()并没有真实删除数据，对象还是存在的，
        //只是断开原来index==0的引用，数组整体向前移动一位。
        Person removed = personList2.remove(0);
        removed.setName("ccc");
        
        /*
        output: removed: Person{name='ccc', age=0} ;personList1.size(): 2 ;personList2.size(): 1
        personList2.remove(0) 只是断开了index == 0 的引用，数据还在内存中。personList1 还是有两条数据。
         */
        System.out.println("removed: " + removed.toString() + " ;personList1.size(): " + personList1.size() + " ;personList2.size(): " + personList2.size());
    }

    private static void testListSub() {
        Person person1 = new Person("aaa");
        Person person2 = new Person("bbb");
        Person person3 = new Person("ccc");

        List<Person> list = new ArrayList<>();
        list.add(person1);
        list.add(person2);
        list.add(person3);
        Utility.printList("list 中的元素是：", list);

        //List#subList() 返回的是 java.util.ArrayList.SubList 类型。
        List<Person> subList = list.subList(1, list.size());
        Utility.printList("subList 中的元素是：", subList);

        //subList 对数据的修改同时会影响到原来的List。
        //其实对 subList的操作都是由parent list 完成的，操作的都是 Object[] elementData 数组。
        Person person = subList.get(0);
        person.setAge(18);
        Utility.printList("subList 修改后原来的list：", list);
        Utility.printList("subList 修改后的数据：", subList);

        //对 parent List 进行添加、删除元素后再操作 Sub List 会抛出 ConcurrentModificationException。
        Person person4 = new Person("ddd");
        list.add(person4);
        Utility.printList(list);
        Utility.printList(subList);
        
    }
}
