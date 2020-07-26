package soy.gabimoreno.audioclean.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class EqualizationTest {

    @Test
    fun `if equalization has 3 frequencies, then the number of bands is 3`() {
        val equalization = Equalization(
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
        val equalization = Equalization(listOf())
        val nBands = equalization.getNBands()
        assertTrue(0 == nBands)
    }

    @Test
    fun `if equalization has some frequencies, then getFrequencies() returns them`() {
        val freq1 = 1
        val freq2 = 2
        val freq3 = 3
        val equalization = Equalization(
            listOf(
                FrequencyGain(freq1, 0),
                FrequencyGain(freq2, 0),
                FrequencyGain(freq3, 0)
            )
        )
        val frequencies = equalization.getFrequencies()
        assertTrue(listOf(freq1, freq2, freq3) == frequencies)
    }

    @Test
    fun `if equalization is empty, then getFrequencies() returns an empty list`() {
        val equalization = Equalization(listOf())
        val frequencies = equalization.getFrequencies()
        assertTrue(emptyList<Int>() == frequencies)
    }
}
