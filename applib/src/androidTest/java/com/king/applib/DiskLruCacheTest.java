package com.king.applib;

import com.king.applib.disklrucache.DiskLruCache;
import com.king.applib.log.Logger;

import org.junit.BeforeClass;

import java.io.IOException;

/**
 * @author huoguangxu
 * @since 2017/4/17.
 */

public class DiskLruCacheTest extends BaseTestCase {

    private static DiskLruCache mDiskLruCache;

    @BeforeClass
    public static void initDiskLruCache() throws IOException {
        mDiskLruCache = DiskLruCache.open(mContext.getCacheDir(), 1, 1, 1024 * 1024 * 50);
    }

    public void testPutString() throws Exception {
        DiskLruCache.Editor editor = mDiskLruCache.edit("string_key");
        editor.set(0, "哈哈哈");
        editor.commit();
    }

    public void testGetString() throws Exception {
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get("string_key");
        if (snapshot != null) {
            Logger.i("result: " + snapshot.getString(0));
        }
    }
}
