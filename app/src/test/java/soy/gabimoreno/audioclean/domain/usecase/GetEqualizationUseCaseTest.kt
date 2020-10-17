package soy.gabimoreno.audioclean.domain.usecase

import org.junit.Assert.assertTrue
import org.junit.Test

class GetEqualizationUseCaseTest {

    @Test
    fun `check frequencies and gains loaded are the correct ones`() {
        val freq0 = 31
        val freq1 = 62
        val freq2 = 125
        val frequencies = intArrayOf(freq0, freq1, freq2)

        val gain0 = 0
        val gain1 = -3
        val gain2 = 6
        val gains = intArrayOf(gain0, gain1, gain2)

        val getEqualizationUseCase = GetEqualizationUseCase()
        val equalization = getEqualizationUseCase("foo", frequencies, gains)

        val frequencyGains = equalization.frequencyGains
        assertTrue(freq0 == frequencyGains[0].frequency)
        assertTrue(freq1 == frequencyGains[1].frequency)
        assertTrue(freq2 == frequencyGains[2].frequency)
        assertTrue(gain0 == frequencyGains[0].gain)
        assertTrue(gain1 == frequencyGains[1].gain)
        assertTrue(gain2 == frequencyGains[2].gain)
    }
}
