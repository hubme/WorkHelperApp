package com.example;

import com.example.entity.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author VanceKing
 * @since 2018/3/21.
 */

public class ListMain {

    public static void main(String[] args) {
        ListMain main = new ListMain();
        main.testEqualsHashCode();
    }

    private void testEqualsHashCode() {
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

    private void testListCopy() {
        Person person1 = new Person("aaa");
        Person person2 = new Person("bbb");

        ArrayList<Person> personList1 = new ArrayList<>();
        personList1.add(person1);
        personList1.add(person2);

        List<Person> personList2 = (ArrayList<Person>) personList1.clone();
        Person removed = personList2.remove(0);
        removed.setName("ccc");
        
        /*
        output: removed: Person{name='ccc', age=0} ;personList1.size(): 2 ;personList2.size(): 1
        personList2.remove(0) 只是断开了index == 0 的引用，数据还在内存中。personList1 还是有两条数据。
         */
        System.out.println("removed: " + removed.toString() + " ;personList1.size(): " + personList1.size() + " ;personList2.size(): " + personList2.size());
    }
}
