package com.king.app.workhelper;

import android.support.annotation.NonNull;
import android.support.v4.util.Pools;
import android.util.Log;

import com.king.applib.log.Logger;

import org.junit.Test;

import java.util.LinkedList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author VanceKing
 * @since 2018/2/28.
 */

public class DataStructureTest extends BaseTestCase {

    @Test
    public void testPool() {
        PersonPool personPool = new PersonPool();
        for (int i = 0; i < 6; i++) {
            Person person = personPool.obtain();
            //观察输出的对象是否一直增加？根据log发现一直重用同一个对象
            Logger.i(String.valueOf(person.toString()));
            personPool.release(person);//使用完后释放
        }
    }

    private static class PersonPool {
        private Pools.SimplePool<Person> personSimplePool = new Pools.SimplePool<>(3);

        public Person obtain() {
            Person person = personSimplePool.acquire();
            return person != null ? person : new Person();
        }

        public boolean release(Person person) {
            try {
                return personSimplePool.release(person);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    @Test
    public void testLinkedList() {
        LinkedList<Person> persons = new LinkedList<>();
        persons.add(new Person("aaa"));
        persons.add(new Person("bbb"));
        persons.add(new Person("ccc"));

        Person polled = persons.poll();
        Logger.i(polled.toString());
        Logger.i(String.valueOf(persons.size()));
        persons.addLast(polled);
        printList(persons);
    }

    public void testDelayQueue() {
        DelayQueue<Person> persons = new DelayQueue<>();
        persons.add(new Person("aaa", 1000));
        persons.add(new Person("bbb", 2000));
        persons.add(new Person("ccc", 3000));

        for (Person person : persons) {
            Log.i("aaa", person.toString());
        }
    }

    private static class Person implements Delayed {
        private String name;
        private long expire;//到期时间

        public Person() {

        }

        public Person(String name) {
            this.name = name;
        }

        public Person(String name, long delay) {
            this.name = name;
            expire = System.currentTimeMillis() + delay;//到期时间 = 当前时间+延迟时间
        }

        @Override public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }

        @Override public long getDelay(@NonNull TimeUnit unit) {
            return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override public int compareTo(@NonNull Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }
    }
}
