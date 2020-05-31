package soy.gabimoreno.audioclean.presenter.customview.fader

import android.widget.SeekBar
import kotlinx.android.synthetic.main.custom_fader.view.*
import soy.gabimoreno.audioclean.domain.ProgressDbMapper

class Fader(private val faderView: FaderView) {

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
        fun onGainChanged(gainDb: Int)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    init {
        faderView.sb.apply {
            sb.min = PROGRESS_MIN
            sb.max = PROGRESS_MAX
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val gainDb = progressDbMapper.getGainDb(progress)
                    listener.onGainChanged(gainDb)
                    showGain(gainDb)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    fun setGain(gainDb: Int) {
        val progress = progressDbMapper.getProgress(gainDb)
        faderView.sb.progress = progress
    }

    fun getGain(): Int {
        val progress = faderView.sb.progress
        return progressDbMapper.getGainDb(progress)
    }

    private fun showGain(gainDb: Int) {
        faderView.tv.text = "$gainDb dB"
    }
}
