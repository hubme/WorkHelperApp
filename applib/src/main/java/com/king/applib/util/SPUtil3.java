package com.king.applib.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * SharedPreferences 工具类。
 *
 * @author VanceKing
 * @since 2019/8/13.
 */
public class SPUtil3 {
    public static final String DEFAULT_SP_NAME = "CCPrefs";

    private SPUtil3() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static SharedPreferences getSP(Context context) {
        return getSP(context, DEFAULT_SP_NAME);
    }

    public static SharedPreferences getSP(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static void putString(Context context, String key, String value) {
        putString(context, DEFAULT_SP_NAME, key, value);
    }

    public static void putString(Context context, String spName, String key, String value) {
        getSP(context, spName).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        return getString(context, DEFAULT_SP_NAME, key);
    }

    public static String getString(Context context, String spName, String key) {
        return getString(context, spName, key, "");
    }

    public static String getString(Context context, String spName, String key, String defValue) {
        return getSP(context, spName).getString(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        putInt(context, DEFAULT_SP_NAME, key, value);
    }

    public static void putInt(Context context, String spName, String key, int value) {
        getSP(context, spName).edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defValue) {
        return getInt(context, DEFAULT_SP_NAME, key, defValue);
    }

    public static int getInt(Context context, String spName, String key, int defValue) {
        return getSP(context, spName).getInt(key, defValue);
    }

    public static void putFloat(Context context, String key, float value) {
        putFloat(context, DEFAULT_SP_NAME, key, value);
    }

    public static void putFloat(Context context, String spName, String key, float value) {
        getSP(context, spName).edit().putFloat(key, value).apply();
    }

    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1.0f);
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getFloat(context, DEFAULT_SP_NAME, key, defValue);
    }

    public static float getFloat(Context context, String spName, String key, float defValue) {
        return getSP(context, spName).getFloat(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        putLong(context, DEFAULT_SP_NAME, key, value);
    }

    public static void putLong(Context context, String spName, String key, long value) {
        getSP(context, spName).edit().putLong(key, value).apply();
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defValue) {
        return getLong(context, DEFAULT_SP_NAME, key, defValue);
    }

    public static long getLong(Context context, String spName, String key, long defValue) {
        return getSP(context, spName).getLong(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        putBoolean(context, DEFAULT_SP_NAME, key, value);
    }

    public static void putBoolean(Context context, String spName, String key, boolean value) {
        getSP(context, spName).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getBoolean(context, DEFAULT_SP_NAME, key, defValue);
    }

    public static boolean getBoolean(Context context, String spName, String key, boolean defValue) {
        return getSP(context, spName).getBoolean(key, defValue);
    }

    public static boolean contains(Context context, String key) {
        return getSP(context).contains(key);
    }

    public static boolean contains(Context context, String spName, String key) {
        return getSP(context, spName).contains(key);
    }

    public static void remove(Context context, String key) {
        remove(context, DEFAULT_SP_NAME, key);
    }

    public static void remove(Context context, String spName, String key) {
        getSP(context, spName).edit().remove(key).apply();
    }

    /**
     * 清空sp
     */
    public static void clear(Context context) {
        clear(context, DEFAULT_SP_NAME);
    }

    public static void clear(Context context, String spName) {
        getSP(context, spName).edit().clear().apply();
    }

    /**
     * 把List转换成Json字符串保存到SP
     */
    public static <T> void putList(Context context, String key, List<T> list) {
        putList(context, DEFAULT_SP_NAME, key, list);
    }

    public static <T> void putList(Context context, String spName, String key, List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        getSP(context, spName).edit().putString(key, JsonUtil.encode(list)).apply();
    }

    /**
     * 把Json字符串传换成相应的List集合
     */
    public static <T> List<T> getList(Context context, String key, Class<T> clazz) {
        return getList(context, DEFAULT_SP_NAME, key, clazz);
    }

    public static <T> List<T> getList(Context context, String spName, String key, Class<T> clazz) {
        return JsonUtil.decodeToList(getString(context, spName, key), clazz);
    }

    /**
     * 保存对象到SP
     */
    public static void putObject(Context context, String key, Object obj) {
        putObject(context, DEFAULT_SP_NAME, key, obj);
    }

    public static void putObject(Context context, String spName, String key, Object obj) {
        getSP(context, spName).edit().putString(key, JsonUtil.encode(obj)).apply();
    }

    /**
     * 从SP中获取相应的对象
     */
    public static <T> T getObject(Context context, String key, Class<T> clazz) {
        return JsonUtil.decode(getString(context, key), clazz);
    }

    public static <T> T getObject(Context context, String spName, String key, Class<T> clazz) {
        return JsonUtil.decode(getString(context, spName, key), clazz);
    }
}
