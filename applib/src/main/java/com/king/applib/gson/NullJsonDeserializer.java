package com.king.applib.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.king.applib.log.Logger;

import java.lang.reflect.Type;

/**
 * @author VanceKing
 * @since 2017/11/3.
 */
public class NullJsonDeserializer implements JsonDeserializer<JsonNull> {
    @Override
    public JsonNull deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json.getAsJsonNull();
        } catch (Exception e) {
            Logger.i("JsonNull");
            return JsonNull.INSTANCE;
        }
    }
}
