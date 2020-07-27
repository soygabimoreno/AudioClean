package soy.gabimoreno.audioclean.domain.usecase

import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.domain.FrequencyGain

class GetEqualizationUseCase() {

    operator fun invoke(frequencies: IntArray, gains: IntArray): Equalization {
        val frequencyGains = mutableListOf<FrequencyGain>()
        frequencies.forEachIndexed { i, frequency ->
            frequencyGains.add(FrequencyGain(frequency, gains[i]))
        }
        return Equalization(frequencyGains)
    }
}
