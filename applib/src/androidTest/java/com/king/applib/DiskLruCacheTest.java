package com.king.applib;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.jakewharton.disklrucache.DiskLruCache;
import com.king.applib.log.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * @author VanceKing
 * @since 2017/4/17.
 */
@RunWith(AndroidJUnit4.class)
public class DiskLruCacheTest extends BaseTestCase {

    private static DiskLruCache mDiskLruCache;

    @BeforeClass
    public static void initDiskLruCache() throws IOException {
        mDiskLruCache = DiskLruCache.open(mContext.getCacheDir(), 1, 2, 1024 * 1024 * 50);
    }

    @Test
    public void testPutString() throws Exception {
        DiskLruCache.Editor editor = mDiskLruCache.edit("string_key");
        editor.set(0, "哈哈哈");
        editor.set(1, "呵呵呵");
        editor.commit();
    }

    @Test
    public void testGetString() throws Exception {
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get("string_key");
        if (snapshot != null) {
            Logger.i("result: " + snapshot.getString(0));
        }
    }
}
