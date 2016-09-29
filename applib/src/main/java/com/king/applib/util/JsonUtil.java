package com.king.applib.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

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
}
