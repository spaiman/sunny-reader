package com.setiawanpaiman.sunnyreader.util

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonParser(private val mGson: Gson) : JsonParser {
    override fun <T> fromJson(json: String, typeOfT: Type): T {
        return mGson.fromJson(json, typeOfT)
    }

    override fun <T> toJson(src: T, typeOfSrc: Type): String {
        return mGson.toJson(src, typeOfSrc)
    }
}