package com.huotu.partnermall.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DateFormat;
import java.util.Date;

public class JSONUtil<T>{
    private static Gson gson = null;

    public static Gson getGson(){
        if (null == gson){
            gson = new GsonBuilder()
                    .serializeNulls()
                    .registerTypeAdapter(Date.class, new DateJsonDeserializer())
                    .registerTypeAdapter(Date.class, new DateJsonDeserializer())
                    .setDateFormat(DateFormat.LONG)
                    .create();
        }
        return gson;
    }

    public String toJson(T t){
        return getGson().toJson(t);
    }

    @SuppressWarnings("unchecked")
    public T toBean(String msg, T t)
    {
        // 这里起初使用
        // Type type = TypeToken<T>() {}.getType());
        // return (T) gson.fromJson(msg,type);
        // 貌似java泛型不具有传递性。只能采用Class参数的方法。有大神可以解决Type的问题请留言告知。THX。
        return (T) getGson().fromJson(msg, t.getClass());
    }

}
