package com.setiawanpaiman.sunnyreader.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonParser implements JsonParser {

    private final Gson mGson;

    public GsonParser(Gson gson) {
        mGson = gson;
    }

    @Override
    public <T> T fromJson(String json, Type typeOfT) {
        return mGson.fromJson(json, typeOfT);
    }

    @Override
    public <T> String toJson(T src, Type typeOfSrc) {
        return mGson.toJson(src, typeOfSrc);
    }
}
