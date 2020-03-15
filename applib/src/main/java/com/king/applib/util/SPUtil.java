package com.king.applib.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * SharedPreferences 工具类.<br/>
 * ps:1.多进程中使用SharedPreference概率性取值出错
 * 2.同一时刻使用apply()方式提交,高概率出现保存不到sp的问题.可以put多次，最后一次apply()提交即可.
 * 3.SharedPreferences不要保存太多的东西，耗内存.
 * Created by VanceKing on 2016/10/19.
 */

public class SPUtil {
    public static final String DEFAULT_SP_NAME = "KingSP";

    private SPUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static SharedPreferences getSP() {
        return getSP(DEFAULT_SP_NAME);
    }

    public static SharedPreferences getSP(String spName) {
        if (StringUtil.isNullOrEmpty(spName)) {
            throw new RuntimeException("sp name not null or empty");
        }
        return ContextUtil.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static void putString(String key, String value) {
        getSP().edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defValue) {
        return getSP().getString(key, defValue);
    }

    public static void putInt(String key, int value) {
        getSP().edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return getInt(key, -1);
    }

    public static int getInt(String key, int defValue) {
        return getSP().getInt(key, defValue);
    }

    public static void putFloat(String key, float value) {
        getSP().edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key) {
        return getFloat(key, -1.0f);
    }

    public static float getFloat(String key, float defValue) {
        return getSP().getFloat(key, defValue);
    }

    public static void putLong(String key, long value) {
        getSP().edit().putLong(key, value).apply();
    }

    public static long getLong(String key) {
        return getLong(key, -1);
    }

    public static long getLong(String key, long defValue) {
        return getSP().getLong(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        getSP().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSP().getBoolean(key, defValue);
    }

    public static boolean contains(String key) {
        return getSP().contains(key);
    }

    public static void remove(String key) {
        getSP().edit().remove(key).apply();
    }

    /** 清空sp */
    public static void clear() {
        getSP().edit().clear().apply();
    }

    /** 把List转换成Json字符串保存到SP */
    public static <T> void putList2Sp(String key, List<T> list) {
        if (ExtendUtil.isListNullOrEmpty(list)) {
            return;
        }
        getSP().edit().putString(key, JsonUtil.encode(list)).apply();
    }

    /** 把Json字符串传换成相应的List集合 */
    public static <T> List<T> getList(String key, Class<T> clazz) {
        final String text = SPUtil.getString(key);
        return JsonUtil.decodeToList(text, clazz);
    }

    /** 保存对象到SP */
    public static void putObject(String key, Object obj) {
        getSP().edit().putString(key, JsonUtil.encode(obj)).apply();
    }

    /** 从SP中获取相应的对象 */
    public static <T> T getObject(String key, Class<T> clazz) {
        return JsonUtil.decode(SPUtil.getString(key), clazz);
    }
}
