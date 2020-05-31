package soy.gabimoreno.audioclean.presenter.customview.fader

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.framework.inflateCustom

class FaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    init {
        inflateCustom(R.layout.custom_fader)
    }
}
