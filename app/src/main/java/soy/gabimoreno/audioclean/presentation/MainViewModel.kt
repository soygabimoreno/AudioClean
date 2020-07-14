package soy.gabimoreno.audioclean.presentation

import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import soy.gabimoreno.audioclean.domain.AudioProcessor
import soy.gabimoreno.audioclean.domain.MediaPlayer
import soy.gabimoreno.audioclean.presentation.customview.fader.Fader

class MainViewModel(
    private val mediaPlayer: MediaPlayer,
    private val audioProcessor: AudioProcessor,
    @RawRes private val resId: Int
) : ViewModel() {

    init {
        mediaPlayer.init(resId)
        audioProcessor.init()
    }

    private val faders = mutableListOf<Fader>()

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

    fun setVolume(i: Int, gain: Int) {
        audioProcessor.setVolume(i, gain)
    }

    fun addFader(fader: Fader) {
        faders.add(fader)
        fader.setListener(object : Fader.Listener {
            override fun onGainChanged(i: Int, gain: Int) {
                setVolume(i, gain)
            }
        })
    }

    fun resetFaders() {
        faders.forEach {
            it.setGain(0)
        }
    }
}
