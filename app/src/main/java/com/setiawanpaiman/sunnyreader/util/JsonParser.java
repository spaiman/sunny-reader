package com.setiawanpaiman.sunnyreader.util;

import java.lang.reflect.Type;

public interface JsonParser {

    <T> T fromJson(String json, Type typeOfT);

    <T> String toJson(T src, Type typeOfSrc);
}