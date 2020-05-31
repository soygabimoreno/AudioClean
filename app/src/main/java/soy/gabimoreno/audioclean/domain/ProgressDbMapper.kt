package soy.gabimoreno.audioclean.domain

class ProgressDbMapper(
    private val gainMin: Int,
    private val gainMax: Int,
    private val progressMin: Int,
    private val progressMax: Int
) {

    fun getGain(progress: Int): Int {
        return progress.mapNumber(progressMin, progressMax, gainMin, gainMax)
    }

    fun getProgress(gain: Int): Int {
        return gain.mapNumber(gainMin, gainMax, progressMin, progressMax)
    }

    private fun Int.mapNumber(inMin: Int, inMax: Int, outMin: Int, outMax: Int): Int {
        return ((this - inMin).toFloat() * (outMax - outMin) / (inMax - inMin) + outMin).toInt()
    }
}
