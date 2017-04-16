package com.king.app.workhelper.common;

import android.support.v4.util.Pools;

/**
 * 对象池的使用
 *
 * @author VanceKing
 * @since 2017/4/16 0016.
 */

public class ObjectPoolSample {
    private static final Pools.SynchronizedPool<ObjectPoolSample> sPool = new Pools.SynchronizedPool<>(5);

    public static ObjectPoolSample obtain() {
        ObjectPoolSample instance = sPool.acquire();
        return instance != null ? instance : new ObjectPoolSample();
    }

    public void recycle() {
        sPool.release(this);
    }
}
