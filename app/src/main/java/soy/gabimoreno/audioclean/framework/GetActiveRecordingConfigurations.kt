package soy.gabimoreno.audioclean.framework

import android.content.Context
import android.media.AudioManager
import android.media.AudioRecordingConfiguration
import arrow.core.Either

class GetActiveRecordingConfigurations(
    private val context: Context
) {

    object NoActiveRecordingConfigurationException : Exception("NoActiveRecordingConfigurationException")

    operator fun invoke(): Either<Throwable, List<AudioRecordingConfiguration>> {
        val audioManager = (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
        val activeRecordingConfigurations = audioManager.activeRecordingConfigurations
        return if (activeRecordingConfigurations.isEmpty()) {
            Either.left(NoActiveRecordingConfigurationException)
        } else {
            Either.right(activeRecordingConfigurations)
        }
    }
}
