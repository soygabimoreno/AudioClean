package soy.gabimoreno.audioclean.framework.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(jsonString: String): T =
    fromJson(jsonString, object : TypeToken<T>() {}.type)
