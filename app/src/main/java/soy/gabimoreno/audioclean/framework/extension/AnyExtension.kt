package soy.gabimoreno.audioclean.framework.extension

import com.google.gson.Gson

fun Any.toJSONString(): String {
    return Gson().toJson(this, Any::class.java)
}
