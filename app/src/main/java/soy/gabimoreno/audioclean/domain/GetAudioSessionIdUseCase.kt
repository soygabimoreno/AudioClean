package soy.gabimoreno.audioclean.domain

import android.content.Context
import android.media.AudioManager

class GetAudioSessionIdUseCase(private val context: Context) {

    operator fun invoke(): Int {
        val audioManager = (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
        return audioManager.generateAudioSessionId()
    }
}
