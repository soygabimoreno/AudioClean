package soy.gabimoreno.audioclean.domain.usecase

import android.content.Context
import android.media.AudioManager

class GetAudioSessionIdUseCase(
    private val context: Context
) {

    sealed class Error(val type: Int) {
        object NoAudioSessionId : Error(-1)
    }

    operator fun invoke(): Int {
        return try {
            val audioManager = (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
            return audioManager.activeRecordingConfigurations[0].clientAudioSessionId - 8
        } catch (e: Exception) {
            Error.NoAudioSessionId.type
        }
    }
}
