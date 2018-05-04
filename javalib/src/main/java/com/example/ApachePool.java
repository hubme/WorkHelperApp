package com.example;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * @author VanceKing
 * @since 2018/5/4.
 */
public class ApachePool {
    public static void main(String[] args) throws Exception {
        test1();
    }

    private static void test1() throws Exception {
        ObjectPool<Person> pool = new GenericObjectPool<>(new PersonPoolFactory());
        System.out.println("对象池信息：" + pool.toString());
        
        Person person = pool.borrowObject();
        if (person != null) {
            System.out.println(person.toString());
        }

        System.out.println("getNumActive(): " + pool.getNumActive());
        System.out.println("getNumIdle(): " + pool.getNumIdle());

        Person person1 = pool.borrowObject();
        if (person1 != null) {
            System.out.println(person1.toString());
        }
        
    }

    private static class PersonPoolFactory extends BasePooledObjectFactory<Person> {

        @Override public Person create() throws Exception {
            return new Person();
        }

        @Override public PooledObject<Person> wrap(Person obj) {
            return new DefaultPooledObject<>(obj);
        }
    }

    private static class Person {

    }
}
