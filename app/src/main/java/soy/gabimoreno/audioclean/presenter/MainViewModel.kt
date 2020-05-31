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
        audioProcessor.init()
    }

    fun isPlayingAudio() = mediaPlayer.isPlaying()

    fun playAudio() {
        mediaPlayer.init(resId)
        mediaPlayer.start()
    }

    fun stopAudio() {
        mediaPlayer.stop()
    }

    fun startProcessing() {
        audioProcessor.start()
    }

    fun stopProcessing() {
        audioProcessor.stop()
    }

    fun getFrequencies(): IntArray {
        return audioProcessor.getFrequencies()
    }

    fun setVolume(gain: Int) {
        audioProcessor.setVolume(gain)
    }

    fun setVolume0(gain: Int) {
        audioProcessor.setVolume0(gain)
    }

    fun setVolume1(gain: Int) {
        audioProcessor.setVolume1(gain)
    }
}