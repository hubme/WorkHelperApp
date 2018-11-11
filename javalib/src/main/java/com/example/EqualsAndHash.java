package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * equals和hashCode的理解<br/>
 * http://www.cnblogs.com/skywang12345/p/3324958.html
 *
 * @author VanceKing
 * @since 2017/3/31.
 */

public class EqualsAndHash {
    public static void main(String[] args) {
        test1();
    }
    

    //重写 equals(),但是没有重写 hashCode(),HashMap 存在多条相同的记录
    // 正确的做法是两个方法都要同时重写
    private static void test1() {
        Person person1 = new Person("123456", "Vance");
        Person person2 = new Person("123456", "Vance");

        System.out.println("person1 == person2: " + (person1 == person2));
        System.out.println("person1.equals(person2): " + person1.equals(person2));

        HashMap<Person, String> map = new HashMap<>();
        map.put(person1, person1.name);
        map.put(person2, person2.name);
        for (Map.Entry<Person, String> entry : map.entrySet()) {
            System.out.println(entry.getKey().toString() + " --- " + entry.getValue());
        }
    }

    private static class Person {
        private String idNumber;
        private String name;

        public Person(String idNumber, String name) {
            this.idNumber = idNumber;
            this.name = name;
        }

        @Override public boolean equals(Object o) {
            if (o == this) {//对象自身，提高效率
                return true;
            }
            // 1. 已经包含 null 判断 
            // 2. 不应该通过 Class 判断，否则将不适合之类继承父类 equals() 的情况
            if (!(o instanceof Person)) {
                return false;
            }
            final Person person = (Person) o;
            //必要的域判断
            return idNumber.equals(person.idNumber);
        }

        @Override public int hashCode() {
            return Objects.hash(idNumber, name);
        }

        @Override public String toString() {
            return "Person{" +
                    "idNumber='" + idNumber + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
