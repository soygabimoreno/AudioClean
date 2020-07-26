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
}
