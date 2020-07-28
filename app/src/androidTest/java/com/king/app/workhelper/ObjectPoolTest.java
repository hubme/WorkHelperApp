package com.king.app.workhelper;

import android.util.Log;

import androidx.core.util.Pools;

import org.junit.Before;
import org.junit.Test;

/**
 * https://commons.apache.org/proper/commons-pool
 *
 * @author VanceKing
 * @since 2018/5/2.
 */
public class ObjectPoolTest extends BaseAndroidJUnit4Test {
    private Pools.SimplePool<SampleModel> sampleModelPool;

    private static class SampleModel {

    }

    @Before
    public void init() {
        //初始化大小为5的数据，数据都为 null。
        sampleModelPool = new Pools.SimplePool<>(5);

    }

    @Test
    public void test2() {
        SampleModel acquire = sampleModelPool.acquire();
        if (acquire == null) {
            acquire = new SampleModel();
        }

        log(acquire);
        sampleModelPool.release(acquire);
        log(sampleModelPool.acquire());
        sampleModelPool.release(acquire);
        //会抛出 IllegalStateException("Already in the pool!");
        sampleModelPool.release(acquire);
    }

    @Test
    public void test1() {
        //没有可以回收使用的对象，返回 null
        SampleModel model1 = sampleModelPool.acquire();
        log(model1);

        SampleModel model2 = new SampleModel();
        log(model2);
        //对象使用完以后回收利用，保存在数组中
        sampleModelPool.release(model2);

        //存在回收利用的对象，直接使用。注意脏数据的问题。
        //同 Message#abtain()，只是 Message 使用单链表保存回收的对象，避免被回收。
        SampleModel model3 = sampleModelPool.acquire();
        log(model3);
    }

    private void log(SampleModel model) {
        Log.i("aaa", model == null ? " == null" : model.toString());
    }

}
