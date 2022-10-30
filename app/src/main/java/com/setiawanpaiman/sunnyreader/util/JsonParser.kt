package com.setiawanpaiman.sunnyreader.util

import java.lang.reflect.Type

interface JsonParser {
    fun <T> fromJson(json: String?, typeOfT: Type?): T
    fun <T> toJson(src: T, typeOfSrc: Type?): String?
}