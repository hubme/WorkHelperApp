package com.king.app.workhelper;

import android.support.v4.util.LruCache;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author VanceKing
 * @since 2018/5/10.
 */
public class LruCacheTest extends BaseAndroidJUnit4Test {

    private LruCache<CacheKey, CacheValue> lruCache;

    @Before
    public void initialize() {
        lruCache = new LruCache<CacheKey, CacheValue>(3) {
            @Override protected CacheValue create(CacheKey key) {
                return new CacheValue("default");
            }

            @Override protected int sizeOf(CacheKey key, CacheValue value) {
                return super.sizeOf(key, value);
            }
        };

    }

    @Test
    public void test1() {
        CacheKey key0 = new CacheKey();
        CacheValue cacheValue0 = lruCache.get(key0);
        logCacheValue(cacheValue0);

        logCacheValue(lruCache.get(key0));

        logCacheValue(lruCache.get(new CacheKey()));
        logCacheValue(lruCache.get(new CacheKey()));
        logCacheValue(lruCache.get(new CacheKey()));
        logCacheValue(lruCache.get(new CacheKey()));
        logCacheValue(lruCache.get(new CacheKey()));
    }

    private static void test2() {
        LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>(0, 0.75f, true);
        hashMap.put(1, "a");
        hashMap.put(2, "b");
        hashMap.put(3, "c");
        hashMap.put(4, "d");

        System.out.println(hashMap.toString());

        hashMap.get(4);
        hashMap.get(3);

        System.out.println(hashMap.toString());
    }

    private static class CacheKey {
        private static final AtomicLong idCreator = new AtomicLong(0);
        private long id;

        public CacheKey() {
            id = idCreator.getAndIncrement();
        }

        @Override public String toString() {
            return "CacheKey{" +
                    "id=" + id +
                    '}';
        }
    }

    private static class CacheValue {
        private String name;

        public CacheValue(String name) {
            this.name = name;
        }

        @Override public String toString() {
            return "CacheValue{" +
                    "name='" + name + '\'' +
                    '}' + Integer.toHexString(hashCode());
        }
    }

    private void logInfo() {
        logMessage("putCount= " + lruCache.putCount() + " createCount= " + lruCache.createCount() + " hitCount= " + lruCache.hitCount());
    }

    private void logCacheValue(CacheValue value) {
        logMessage(value == null ? "value == null" : value.toString());
    }
}
