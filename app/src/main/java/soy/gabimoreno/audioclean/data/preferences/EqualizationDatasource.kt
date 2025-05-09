package soy.gabimoreno.audioclean.data.preferences

import android.content.SharedPreferences
import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.google.gson.Gson
import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.framework.extension.fromJson
import soy.gabimoreno.audioclean.framework.extension.toJSONString
import soy.gabimoreno.audioclean.framework.extension.unsafeCatch
import androidx.core.content.edit

class EqualizationDatasource(
    private val sharedPreferences: SharedPreferences
) {

    enum class Positions {
        POSITION_0,
        POSITION_1,
        POSITION_2,
        POSITION_3,
        POSITION_4,
        POSITION_5,
        POSITION_6,
        POSITION_7,
        POSITION_8,
        POSITION_9
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
        Positions.entries.forEach {
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
        return list.right()
    }

    fun save(equalization: Equalization) {
        run loop@{
            Positions.entries.forEach { equalizationDatasource ->
                if (sharedPreferences.getString(equalizationDatasource.name, "") == "") {
                    sharedPreferences
                        .edit() {
                            putString(equalizationDatasource.name, equalization.toJSONString())
                        }
                    return@loop
                }
            }
        }
    }

    fun deleteAll() {
        sharedPreferences
            .edit() {
                clear()
            }
    }
}
