package soy.gabimoreno.audioclean.domain

data class Equalization(
    val frequencyGains: List<FrequencyGain>
) {
    fun getNBands(): Int = frequencyGains.size

    fun getFrequencies(): List<Int> = frequencyGains.map { it.frequency }
}
