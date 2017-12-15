package com.u2sim.tellwechat.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by hanguojing on 2017/11/28 14:24
 */

public class GsonFormatUtil {
    private volatile static GsonFormatUtil instance;
    private Gson gson;

    public static GsonFormatUtil getInstance() {
        if (instance == null) {
            synchronized (GsonFormatUtil.class) {
                if (instance == null) {
                    instance = new GsonFormatUtil();
                }
            }
        }
        return instance;
    }


    private GsonFormatUtil() {
        gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>()).create();
//        gson = new Gson();

    }

    public String toJson(Object src) {
        return src == null ? null : gson.toJson(src);
    }

    public String toJson(Object src, Type typeOfSrc) {
        return src == null ? null : gson.toJson(src, typeOfSrc);
    }

    public <T> T fromJson(String json, Class<T> clz) {
        return TextUtils.isEmpty(json) ? null : gson.fromJson(json, clz);
    }
    public <T> T fromJson(JsonElement jsonElement,Class<T> clz){
        return jsonElement==null ? null : gson.fromJson(jsonElement, clz);
    }

    public Object fromJson(String json,Type typeOfObj){
        return TextUtils.isEmpty(json) ? null : gson.fromJson(json,typeOfObj);
    }

    static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();

            if(rawType != String.class){
                return null;
            }
            return (TypeAdapter<T>) new StringNullAdapter();
        }
    }
    static class StringNullAdapter extends TypeAdapter<String> {

        @Override
        public void write(JsonWriter out, String value) throws IOException {

            out.value(value == null?"":value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            if(in.peek() == JsonToken.NULL){
                in.nextNull();
                return "";
            }
            return in.nextString();
        }
    }

}
