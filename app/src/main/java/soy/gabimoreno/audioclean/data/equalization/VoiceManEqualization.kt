package soy.gabimoreno.audioclean.data.equalization

import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.domain.Frequencies
import soy.gabimoreno.audioclean.domain.FrequencyGain

class VoiceManEqualization {

    fun get() = Equalization(
        listOf(
            FrequencyGain(Frequencies.FREQ_31.value, -12),
            FrequencyGain(Frequencies.FREQ_62.value, -12),
            FrequencyGain(Frequencies.FREQ_125.value, 0),
            FrequencyGain(Frequencies.FREQ_250.value, 3),
            FrequencyGain(Frequencies.FREQ_500.value, 0),
            FrequencyGain(Frequencies.FREQ_1000.value, 3),
            FrequencyGain(Frequencies.FREQ_2000.value, 2),
            FrequencyGain(Frequencies.FREQ_4000.value, 6),
            FrequencyGain(Frequencies.FREQ_8000.value, 0),
            FrequencyGain(Frequencies.FREQ_16000.value, 0)
        )
    )
}
