package soy.gabimoreno.audioclean.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class EqualizationTest {

    @Test
    fun `if equalization has 3 frequencies, then the number of bands is 3`() {
        val equalization = Equalization(
            "foo",
            listOf(
                FrequencyGain(1, 0),
                FrequencyGain(2, 0),
                FrequencyGain(3, 0)
            )
        )
        val nBands = equalization.getNBands()
        assertTrue(3 == nBands)
    }

    @Test
    fun `if equalization is empty, then the number of bands is 0`() {
        val equalization = Equalization("foo", listOf())
        val nBands = equalization.getNBands()
        assertTrue(0 == nBands)
    }

    @Test
    fun `if equalization has some frequencies, then getFrequencies() returns them`() {
        val freq1 = 1
        val freq2 = 2
        val freq3 = 3
        val equalization = Equalization(
            "foo",
            listOf(
                FrequencyGain(freq1, 0),
                FrequencyGain(freq2, 0),
                FrequencyGain(freq3, 0)
            )
        )
        val frequencies = equalization.getFrequencies()
        assertTrue(intArrayOf(freq1, freq2, freq3).contentEquals(frequencies))
    }

    @Test
    fun `if equalization is empty, then getFrequencies() returns an empty list`() {
        val equalization = Equalization("foo", listOf())
        val frequencies = equalization.getFrequencies()
        assertTrue(intArrayOf().contentEquals(frequencies))
    }

    @Test
    fun `if equalization has some gains, then getGains() returns them`() {
        val gain1 = 0
        val gain2 = -3
        val gain3 = 6
        val equalization = Equalization(
            "foo",
            listOf(
                FrequencyGain(1, 0),
                FrequencyGain(2, -3),
                FrequencyGain(3, 6)
            )
        )
        val gains = equalization.getGains()
        assertTrue(intArrayOf(gain1, gain2, gain3).contentEquals(gains))
    }

    @Test
    fun `if equalization is empty, then getGains() returns an empty list`() {
        val equalization = Equalization("foo", listOf())
        val gains = equalization.getGains()
        assertTrue(intArrayOf().contentEquals(gains))
    }
}
