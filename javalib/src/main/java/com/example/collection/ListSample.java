package com.example.collection;

import com.example.entity.Person;
import com.example.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * @author VanceKing
 * @since 2018/3/21.
 */

public class ListSample {

    public static void main(String[] args) {
        test3();
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

    private static void testListIterator() {
        List<Person> arrayList = new ArrayList<>();
        arrayList.add(new Person("aaa"));
        arrayList.add(new Person("bbb"));
        arrayList.add(new Person("bbb"));
        arrayList.add(new Person("ccc"));

        for (Person person : arrayList) {
            if ("bbb".equals(person.getName())) {
                arrayList.remove(person);
            }
        }
        
        /*Iterator<Person> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Person next = iterator.next();
            if ("bbb".equals(next.getName())) {
                iterator.remove();//iterator 动态删除
            }
        }*/
        Utility.printList(arrayList);
    }

    private static void testCollectionsComparator() {
        List<Integer> data = Arrays.asList(1, 20, 3);
        //通过 Iterator 比较
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
        System.out.println(String.valueOf(aaa));
    }

    private static void test1() {
        //基本类型是不能作为泛型参数的,Arrays.asList()只能接收引用类型，自然为了编译通过编译器就把上面的 int[] 数组当做了一个引用参数
        int[] arrays1 = {1, 2, 3};
        List<int[]> list1 = Arrays.asList(arrays1);
        System.out.println("size1: " + list1.size());//size == 1

        Integer[] arrays2 = {1, 2, 3};
        List<Integer> list2 = Arrays.asList(arrays2);
        System.out.println("size2: " + list2.size());//size == 3

        // 会抛出 java.lang.UnsupportedOperationException
        // 因为 Arrays.asList() 返回的是 java.util.Arrays.ArrayList 类型,不是 java.util.ArrayList 类型，没有 add() 等方法。
//        list2.add(4);

        //类型转换失败
//        ArrayList<Integer> integers = Arrays.asList(1, 2, 3);
    }

    private static void test2() {
        ArrayList<String> list = new ArrayList<>();
        list.add("android");
        
        Vector<String> vector = new Vector<>();
        vector.add("android");
        
        System.out.println("is equals ? " + list.equals(vector));
    }

    private static void test3() {
        Set<Person> persons = new HashSet<>();
        Person aPerson = new Person("aaa", 18);
        persons.add(aPerson);
        persons.add(new Person("bbb", 19));
        
        //属性值修改后hash值也和以前不同了，所以HashSet#remove()时找不到原来的对象了。
        aPerson.setAge(20);

        System.out.println("size of persons : " + persons.size());
        persons.remove(aPerson);
        System.out.println("size of persons : " + persons.size());
        
        
    }
}
