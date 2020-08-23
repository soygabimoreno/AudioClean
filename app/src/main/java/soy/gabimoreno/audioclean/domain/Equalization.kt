package soy.gabimoreno.audioclean.domain

data class Equalization(
//    val name: String,
    val frequencyGains: List<FrequencyGain>
) {
    fun getNBands(): Int = frequencyGains.size
    fun getFrequencies(): IntArray = frequencyGains.map { it.frequency }.toIntArray()
    fun getGains(): IntArray = frequencyGains.map { it.gain }.toIntArray()
}
