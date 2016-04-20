package com.huotu.partnermall.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/19.
 */
public class DateJsonDeserializer implements JsonDeserializer<Date> ,JsonSerializer<Date> {
    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Date( jsonElement.getAsJsonPrimitive().getAsLong() );
    }
    @Override
    public JsonElement serialize(Date date , Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive( date.getTime() );
    }
}
