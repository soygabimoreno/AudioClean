package soy.gabimoreno.audioclean.data

import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.domain.Frequencies
import soy.gabimoreno.audioclean.domain.FrequencyGain

class DefaultEqualization {

    fun get() = Equalization(
        listOf(
            FrequencyGain(Frequencies.FREQ_31.value, 0),
            FrequencyGain(Frequencies.FREQ_62.value, 0),
            FrequencyGain(Frequencies.FREQ_125.value, 0),
            FrequencyGain(Frequencies.FREQ_250.value, 0),
            FrequencyGain(Frequencies.FREQ_500.value, 0),
            FrequencyGain(Frequencies.FREQ_1000.value, 0),
            FrequencyGain(Frequencies.FREQ_2000.value, 0),
            FrequencyGain(Frequencies.FREQ_4000.value, 0),
            FrequencyGain(Frequencies.FREQ_8000.value, 0),
            FrequencyGain(Frequencies.FREQ_16000.value, 0)
        )
    )
}
