package soy.gabimoreno.audioclean.presenter

import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import soy.gabimoreno.audioclean.domain.AudioProcessor
import soy.gabimoreno.audioclean.domain.MediaPlayer

class MainViewModel(
    private val mediaPlayer: MediaPlayer,
    private val audioProcessor: AudioProcessor,
    @RawRes private val resId: Int
) : ViewModel() {

    init {
        mediaPlayer.init(resId)
    }

    fun isPlaying() = mediaPlayer.isPlaying()

    fun start() {
        mediaPlayer.init(resId)
        mediaPlayer.start()
    }

    fun stop() {
        mediaPlayer.stop()
    }
}
