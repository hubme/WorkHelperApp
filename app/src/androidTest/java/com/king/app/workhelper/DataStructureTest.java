package com.king.app.workhelper;

import android.support.v4.util.Pools;

import com.king.applib.log.Logger;

import org.junit.Test;

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

    private static class PersonPool{
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


    private static class Person {
        private String name;
    }
}
