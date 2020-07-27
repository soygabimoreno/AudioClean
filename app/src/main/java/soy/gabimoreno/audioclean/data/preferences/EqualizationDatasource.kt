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

    companion object {
        private const val EQUALIZATION_DATASOURCE = "EQUALIZATION_DATASOURCE"
    }

    private val gson: Gson by lazy { Gson() }

    fun load(): Either<Throwable, Equalization> = Either.unsafeCatch {
        sharedPreferences.getString(EQUALIZATION_DATASOURCE, "")
    }.flatMap { jsonString ->
        Either.unsafeCatch {
            gson.fromJson<Equalization>(jsonString!!)
        }
    }

    fun save(equalization: Equalization) {
        sharedPreferences.edit()
            .putString(EQUALIZATION_DATASOURCE, equalization.toJSONString())
            .apply()
    }
}
