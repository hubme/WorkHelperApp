package com.king.applib.util;

import com.king.applib.log.Logger;

import java.util.List;

/**
 * 扩展工具类
 * Created by HuoGuangxu on 2016/9/29.
 */

public class ExtendUtil {
    private ExtendUtil() {

    }

    public static boolean isListNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static <E> boolean isArrayNullOrEmpty(E[] array) {
        return array == null || array.length == 0;
    }

    public static <E> void printArray(E[] array) {
        if (ExtendUtil.isArrayNullOrEmpty(array)) {
            return;
        }
        for (E element : array) {
            if (element != null) {
                Logger.i(element.toString());
            }
        }

    }

    public static <T> void printList(List<T> list) {
        if (ExtendUtil.isListNullOrEmpty(list)) {
            return;
        }
        for (T t : list) {
            if (t != null) {
                Logger.i(t.toString());
            }
        }
    }
}
