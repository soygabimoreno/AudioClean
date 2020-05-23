package soy.gabimoreno.audioclean.domain

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

class MediaPlayer(private val context: Context) {

    private lateinit var mediaPlayer: MediaPlayer

    private var sessionId = 0

    fun init(@RawRes resId: Int) {
        mediaPlayer = MediaPlayer.create(context, resId)
        sessionId = mediaPlayer.audioSessionId
    }

    fun start() {
        mediaPlayer.start()
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun isPlaying(): Boolean = mediaPlayer.isPlaying
}
