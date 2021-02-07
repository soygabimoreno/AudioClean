package soy.gabimoreno.audioclean.presentation.customview.fader

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import soy.gabimoreno.audioclean.databinding.CustomFaderBinding

class FaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var binding: CustomFaderBinding = CustomFaderBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun showGain(gain: Int) {
        binding.tvGain.text = "$gain"
    }

    fun showMagnitude(frequency: Int) {
        if (frequency < 1000) {
            binding.tvMagnitude.text = "$frequency"
        } else {
            binding.tvMagnitude.text = "${frequency / 1000}k"
        }
    }
}
