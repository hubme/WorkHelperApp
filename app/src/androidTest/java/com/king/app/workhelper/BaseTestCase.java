package com.king.app.workhelper;

import android.content.Context;
import android.test.AndroidTestCase;

import com.king.applib.log.Logger;
import com.king.applib.util.ContextUtil;
import com.king.applib.util.ExtendUtil;

import java.util.List;

/**
 * TestCase基础类
 * Created by HuoGuangxu on 2016/11/30.
 */

public class BaseTestCase extends AndroidTestCase {
    protected Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();
        ContextUtil.init(mContext);
        Logger.init("aaa").methodCount(1).hideThreadInfo();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
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
