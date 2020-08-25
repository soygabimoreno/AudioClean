package soy.gabimoreno.audioclean.data.preferences

import android.content.SharedPreferences
import arrow.core.Either
import arrow.core.flatMap
import com.google.gson.Gson
import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.framework.extension.fromJson
import soy.gabimoreno.audioclean.framework.extension.toJSONString
import soy.gabimoreno.audioclean.framework.extension.unsafeCatch

class EqualizationDatasource(
    private val sharedPreferences: SharedPreferences
) {

    enum class Positions {
        ZERO,
        ONE,
        TWO
    }

    private val gson: Gson by lazy { Gson() }

    fun load(position: Positions): Either<Throwable, Equalization> = Either.unsafeCatch {
        sharedPreferences.getString(position.name, "")
    }.flatMap { jsonString ->
        Either.unsafeCatch {
            gson.fromJson<Equalization>(jsonString!!)
        }
    }

    fun loadAll(): Either<Throwable, List<Equalization>> {
        val list = mutableListOf<Equalization>()
        Positions.values().forEach {
            Either.unsafeCatch {
                sharedPreferences.getString(it.name, "")
            }.flatMap { jsonString ->
                Either.unsafeCatch {
                    val item = gson.fromJson<Equalization>(jsonString!!)
                    if (item != null) {
                        list.add(item)
                    }
                }
            }

        }
        return Either.right(list)
    }

    fun save(equalization: Equalization) {
        run loop@{
            Positions.values().forEach { equalizationDatasource ->
                if (sharedPreferences.getString(equalizationDatasource.name, "") == "") {
                    sharedPreferences.edit()
                        .putString(equalizationDatasource.name, equalization.toJSONString())
                        .apply()
                    return@loop
                }
            }
        }
    }
}
