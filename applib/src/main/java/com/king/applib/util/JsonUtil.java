package com.king.applib.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Json字符串和对象相互转化的工具类
 * created by VanceKing at 2016/9/29
 */

public class JsonUtil {
    private static final Gson GSON = new Gson();

    private JsonUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    /**
     * 将json字符串转换成相应的对象
     */
    public static <T> T decode(String jsonString, Class<T> clazz) {
        if (StringUtil.isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            return GSON.fromJson(jsonString, clazz);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * 将json字符串转换成相应的对象
     */
    public static <T> T decode(String jsonString, Type typeOfT) {
        if (StringUtil.isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            return GSON.fromJson(jsonString, typeOfT);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * 将json字符串转换成相应的对象集合
     *
     * @param jsonString 必须是能转换成JSON Array的字符串，否则转换失败，返回null
     */
    public static <T> List<T> decodeToList(String jsonString, Class<T> clazz) {
        if (StringUtil.isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            List<T> list = new ArrayList<>();
            JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(GSON.fromJson(elem, clazz));
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将一个对象转换成json字符串
     */
    public static String encode(Object object) {
        try {
            return GSON.toJson(object);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 从指定的JSONObject获取最内层的JSONObject,如果中间环节获取JSONObject == null,则直接返回null.
     *
     * @param jsonObject 指定的JSONObject
     * @param names      json字符串从外层到内层的name
     * @return 最内层的name的JSONObject
     */
    public static JSONObject getJsonObject(final JSONObject jsonObject, String... names) {
        if (jsonObject == null || names == null || names.length == 0) {
            return null;
        }
        JSONObject tempJsonObject = jsonObject;
        for (int i = 0; i < names.length; i++) {
            if (tempJsonObject != null) {
                tempJsonObject = tempJsonObject.optJSONObject(names[i]);
            } else {
                return null;
            }
            if (i == names.length - 1) {
                return tempJsonObject;
            }
        }
        return null;
    }

    /** 从指定的JSONObject获取name的JSONArray */
    public static JSONArray getJsonArray(final JSONObject jsonObject, String name) {
        return jsonObject == null ? null : jsonObject.optJSONArray(name);
    }

    /** 从JSONObject中获取name的内容 */
    public static String getString(JSONObject jsonObject, String name) {
        return jsonObject == null ? "" : jsonObject.optString(name, "");
    }

    /** 从JSONObject中获取name的内容 */
    public static String getString(JSONObject jsonObject, String name, String defaultValue) {
        return jsonObject == null ? defaultValue : jsonObject.optString(name, defaultValue);
    }

    /** 从JSONObject中获取name的值 */
    public static int getInt(JSONObject jsonObject, String name) {
        return getInt(jsonObject, name, 0);
    }

    /** 从JSONObject中获取name的值 */
    public static int getInt(JSONObject jsonObject, String name, int defaultValue) {
        return jsonObject == null ? defaultValue : jsonObject.optInt(name, defaultValue);
    }
}
