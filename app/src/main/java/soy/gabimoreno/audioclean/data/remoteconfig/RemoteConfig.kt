package soy.gabimoreno.audioclean.data.remoteconfig

import arrow.core.Either
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import soy.gabimoreno.audioclean.data.analytics.error.ErrorTrackerComponent

class RemoteConfig(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    errorTrackerComponent: ErrorTrackerComponent
) {

    companion object {
        private const val AMPLITUDE_API_KEY_AUDIO_CLEAN = "AMPLITUDE_API_KEY_AUDIO_CLEAN"
    }

    init {
        val defaults = mapOf(
            AMPLITUDE_API_KEY_AUDIO_CLEAN to ""
        )

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(defaults)
        firebaseRemoteConfig
            .fetchAndActivate()
            .addOnFailureListener {
                errorTrackerComponent.trackError(it)
            }
    }

    suspend fun getAmplitudeApiKey(): Either<Throwable, String> = withContext(Dispatchers.IO) {
        Either.catch { firebaseRemoteConfig.getString(AMPLITUDE_API_KEY_AUDIO_CLEAN) }
    }
}
