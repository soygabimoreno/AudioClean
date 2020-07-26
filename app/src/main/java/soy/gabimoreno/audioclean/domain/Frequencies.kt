package soy.gabimoreno.audioclean.domain

enum class Frequencies(
    val value: Int
) {
    FREQ_31(31),
    FREQ_62(62),
    FREQ_125(125),
    FREQ_250(250),
    FREQ_500(500),
    FREQ_1000(1000),
    FREQ_2000(2000),
    FREQ_4000(4000),
    FREQ_8000(8000),
    FREQ_16000(16000)
}
