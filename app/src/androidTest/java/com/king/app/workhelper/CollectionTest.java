package com.king.app.workhelper;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;

import com.king.applib.log.Logger;

import org.junit.Test;

/**
 * 集合相关测试类
 * @author VanceKing
 * @since 2018/1/23.
 */

public class CollectionTest extends BaseAndroidJUnit4Test {
    private static class ArrayMapKey{
        private int key;

        public ArrayMapKey() {
            
        }
        
        public ArrayMapKey(int key) {
            this.key = key;
        }

        @Override public String toString() {
            return "ArrayMapKey{" +
                    "key=" + key +
                    '}';
        }
    }
    
    @Test
    public void testSimpleArrayMap() {
        SimpleArrayMap<ArrayMapKey, String> simpleArrayMap = new ArrayMap<>();
        for (int i = 0; i < 5; i++) {
            //Rect 重写了 equals
            simpleArrayMap.put(new ArrayMapKey(), "value_" + i);
        }
        Logger.i(simpleArrayMap.size() + "");
    }
}
