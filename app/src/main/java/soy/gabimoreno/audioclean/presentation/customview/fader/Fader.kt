package soy.gabimoreno.audioclean.presentation.customview.fader

import android.widget.SeekBar
import kotlinx.android.synthetic.main.custom_fader.view.*
import soy.gabimoreno.audioclean.domain.ProgressDbMapper

class Fader(
    private val faderView: FaderView,
    i: Int,
    frequency: Int
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
        faderView.showGain(0)
        setGain(0)
        faderView.showMagnitude(frequency)
        faderView.sb.apply {
            sb.min = PROGRESS_MIN
            sb.max = PROGRESS_MAX
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val gain = progressDbMapper.getGain(progress)
                    listener.onGainChanged(i, gain)
                    faderView.showGain(gain)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    fun setGain(gain: Int) {
        val progress = progressDbMapper.getProgress(gain)
        faderView.sb.progress = progress
    }

    fun getGain(): Int {
        val progress = faderView.sb.progress
        return progressDbMapper.getGain(progress)
    }
}
