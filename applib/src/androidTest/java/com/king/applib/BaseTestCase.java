package com.king.applib;

import com.king.applib.log.Logger;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.ExtendUtil;

import org.junit.BeforeClass;

import java.util.List;

/**
 * TestCase基础类
 * Created by VanceKing on 2016/11/30.
 */

abstract class BaseTestCase extends LibBaseAndroidJUnit4Test {

    @BeforeClass
    public static void initContext() {
        ContextUtil.init(mContext);
    }

    protected <E> void printList(List<E> list) {
        if (ExtendUtil.isListNullOrEmpty(list)) {
            return;
        }
        for (E e : list) {
            if (e != null) {
                Logger.i(e.toString());
            }
        }
    }

    protected <E> void printArray(E[] array) {
        if (ExtendUtil.isArrayNullOrEmpty(array)) {
            return;
        }
        for (E element : array) {
            if (element != null) {
                Logger.i(element.toString());
            }
        }
    }
}
