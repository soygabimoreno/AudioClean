package soy.gabimoreno.audioclean.presenter.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.custom_fader.view.*
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.framework.inflateCustom

class Fader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    init {
        inflateCustom(R.layout.custom_fader)
    }

    fun setDb(dB: Int) {
        tv.text = "$dB dB"
    }
}
