package soy.gabimoreno.audioclean.presenter.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.custom_fader.view.*
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.framework.inflateCustom

class Fader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private lateinit var listener: Listener

    interface Listener {
        fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    init {
        inflateCustom(R.layout.custom_fader)
        sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener.onProgressChanged(seekBar, progress, fromUser)
                setDb(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun setDb(dB: Int) {
        tv.text = "$dB dB"
    }
}
