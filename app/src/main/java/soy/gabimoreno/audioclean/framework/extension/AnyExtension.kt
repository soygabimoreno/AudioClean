package soy.gabimoreno.audioclean.framework.extension

import com.google.gson.Gson
import org.json.JSONObject

val Any?.exhaustive
    get() = Unit

fun Any.toJSONString(): String {
    return Gson().toJson(this, Any::class.java)
}

fun Any.toJSONObject(): JSONObject {
    return JSONObject(Gson().toJson(this, Any::class.java))
}
