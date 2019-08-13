package com.king.applib.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 工具类。
 *
 * @author VanceKing
 * @since 2019/8/13.
 */
public class SPUtil2 {
    public static final String DEFAULT_SP_NAME = "CCPrefs";

    private SPUtil2() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static SharedPreferences getSP(Context context) {
        return getSP(context, DEFAULT_SP_NAME);
    }

    public static SharedPreferences getSP(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static Object get(Context context, String key, Object defaultObject) {
        return get(context, DEFAULT_SP_NAME, key, defaultObject);
    }

    public static Object get(Context context, String spName, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public static void put(Context context, String key, Object object) {
        put(context, DEFAULT_SP_NAME, key, object);
    }

    public static void put(Context context, String spName, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        editor.apply();
    }
}
