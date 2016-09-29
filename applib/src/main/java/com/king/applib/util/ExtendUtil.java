package com.king.applib.util;

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
}
