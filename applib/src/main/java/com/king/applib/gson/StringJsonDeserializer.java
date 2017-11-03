package com.king.applib.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.king.applib.log.Logger;

import java.lang.reflect.Type;

/**
 * @author VanceKing
 * @since 2017/11/3.
 */
public class StringJsonDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            final String text = json.getAsString();
            Logger.i(text);
            return text;
        } catch (Exception e) {
            Logger.i("StringJsonDeserializer-deserialize-error:" + (json != null ? json.toString() : ""));
            return "null or empty";
        }
    }
}
