package com.king.applib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SharedPreferences 工具类.<br/>
 * ps:1.多进程中使用SharedPreference概率性取值出错
 * 2.同一时刻使用apply()方式提交,高概率出现保存不到sp的问题.可以put多次，最后一次apply()提交即可.
 * Created by HuoGuangxu on 2016/10/19.
 */

public class SPUtil {
    public static final String SP_NAME = "KingSP";
    public static final String SP_SEPARATOR = ";##;";

    private SPUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static SharedPreferences getSP(@NonNull Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static void putString(Context context, String key, String value) {
        getSP(context).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static String getString(Context context, String key, @NonNull String defValue) {
        return getSP(context).getString(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        getSP(context).edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSP(context).getInt(key, defValue);
    }

    public static void putFloat(Context context, String key, float value) {
        getSP(context).edit().putFloat(key, value).apply();
    }

    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1.0f);
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getSP(context).getFloat(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        getSP(context).edit().putLong(key, value).apply();
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSP(context).getLong(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSP(context).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSP(context).getBoolean(key, defValue);
    }

    public static boolean contains(Context context, String key) {
        return getSP(context).contains(key);
    }

    public static void remove(Context context, String key) {
        getSP(context).edit().remove(key).apply();
    }

    /**
     * 清空sp
     */
    public static void clear(Context context) {
        getSP(context).edit().clear().apply();
    }

    /**
     * 保存List<String>到sp.以{@link SPUtil#SP_SEPARATOR}分割,所以字符串中不要包含{@link SPUtil#SP_SEPARATOR},否则保存的和读取的不一致。
     */
    public static void putStringList(@NonNull Context context, String key, List<String> list) {
        if (ExtendUtil.isListNullOrEmpty(list)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (StringUtil.isNullOrEmpty(str)) {
                sb.append(" ");
            } else {
                sb.append(str);
            }
            sb.append(SP_SEPARATOR);
        }
        getSP(context).edit().putString(key, sb.toString()).apply();
    }

    /**
     * 获取指定key的List<String>
     */
    public static List<String> getStringList(Context context, String key) {
        String textList = getString(context, key);
        if (StringUtil.isNullOrEmpty(textList)) {
            return null;
        }
        String[] stringArray = textList.trim().split(SP_SEPARATOR);
        return Arrays.asList(stringArray);
    }

    /**
     * 保存List<Integer>到sp.以{@link SPUtil#SP_SEPARATOR}分割,所以字符串中不要包含{@link SPUtil#SP_SEPARATOR},否则保存和读取的不一致。
     */
    public static void putIntList(Context context, String key, List<Integer> list) {
        if (ExtendUtil.isListNullOrEmpty(list)) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int value : list) {
            sb.append(value).append(SP_SEPARATOR);
        }
        getSP(context).edit().putString(key, sb.toString()).apply();
    }

    /**
     * 获取指定key的List<Integer>.
     */
    public static List<Integer> getIntList(Context context, String key) {
        String intList = getString(context, key);
        if (StringUtil.isNullOrEmpty(intList)) {
            return null;
        }
        List<Integer> values = new ArrayList<>();
        String[] intArray = intList.trim().split(SP_SEPARATOR);
        for (String value : intArray) {
            values.add(NumberUtil.getInt(value));
        }
        return values;
    }
}
