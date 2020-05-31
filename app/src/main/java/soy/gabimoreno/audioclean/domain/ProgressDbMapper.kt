package soy.gabimoreno.audioclean.domain

class ProgressDbMapper(
    private val gainDbMin: Int,
    private val gainDbMax: Int,
    private val progressMin: Int,
    private val progressMax: Int
) {

    fun getGainDb(progress: Int): Int {
        return progress.mapNumber(progressMin, progressMax, gainDbMin, gainDbMax)
    }

    fun getProgress(gainDb: Int): Int {
        return gainDb.mapNumber(gainDbMin, gainDbMax, progressMin, progressMax)
    }

    private fun Int.mapNumber(inMin: Int, inMax: Int, outMin: Int, outMax: Int): Int {
        return ((this - inMin).toFloat() * (outMax - outMin) / (inMax - inMin) + outMin).toInt()
    }
}
