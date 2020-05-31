package soy.gabimoreno.audioclean.presenter.customview.fader

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.custom_fader.view.*
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.framework.inflateCustom

class FaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    init {
        inflateCustom(R.layout.custom_fader)
    }

    fun showGain(gain: Int) {
        tvGain.text = "$gain dB"
    }

    fun showMagnitude(frequency: Int) {
        if (frequency < 1000) {
            tvMagnitude.text = "$frequency Hz"
        } else {
            tvMagnitude.text = "${frequency / 1000} kHz"
        }
    }
}
