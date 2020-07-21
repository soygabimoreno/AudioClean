package soy.gabimoreno.audioclean.domain.usecase

import android.content.Context
import android.media.AudioManager

class GetAudioSessionIdUseCase(
    private val context: Context
) {

    companion object {
        private const val AUDIO_SESSION_ID_STEP = 8
    }

    sealed class Error(val type: Int) {
        object NoActiveRecordingConfiguration : Error(-1)
        object NoAudioSessionId : Error(-2)
    }

    operator fun invoke(): Int {
        return try {
            val audioManager = (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
            val activeRecordingConfigurations = audioManager.activeRecordingConfigurations
            return if (activeRecordingConfigurations.isEmpty()) {
                Error.NoActiveRecordingConfiguration.type
            } else {
                activeRecordingConfigurations[0].clientAudioSessionId - AUDIO_SESSION_ID_STEP
            }
        } catch (e: Exception) {
            Error.NoAudioSessionId.type
        }
    }
}
