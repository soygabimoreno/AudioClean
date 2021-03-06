package soy.gabimoreno.audioclean.presentation.customview.fader

import android.widget.SeekBar
import soy.gabimoreno.audioclean.domain.ProgressDbMapper

class Fader(
    private val faderView: FaderView,
    i: Int,
    val frequency: Int,
    gain: Int
) {

    companion object {
        const val GAIN_DB_MIN = -12
        const val GAIN_DB_MAX = 12
        private const val PROGRESS_MIN = 0
        private const val PROGRESS_MAX = 100
    }

    private val progressDbMapper = ProgressDbMapper(
        GAIN_DB_MIN,
        GAIN_DB_MAX,
        PROGRESS_MIN,
        PROGRESS_MAX
    )

    private lateinit var listener: Listener

    interface Listener {
        fun onGainChanged(i: Int, gain: Int)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    init {
        faderView.showGain(gain)
        setGain(gain)
        faderView.showMagnitude(frequency)
        faderView.binding.sb.apply {
            faderView.binding.sb.min = PROGRESS_MIN
            faderView.binding.sb.max = PROGRESS_MAX
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val currentGain = progressDbMapper.getGain(progress)
                    listener.onGainChanged(
                        i,
                        currentGain
                    )
                    faderView.showGain(currentGain)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    fun setGain(gain: Int) {
        val progress = progressDbMapper.getProgress(gain)
        faderView.binding.sb.progress = progress
    }

    fun getGain(): Int {
        val progress = faderView.binding.sb.progress
        return progressDbMapper.getGain(progress)
    }
}
