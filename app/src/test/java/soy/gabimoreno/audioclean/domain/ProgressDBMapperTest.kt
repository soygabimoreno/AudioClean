package soy.gabimoreno.audioclean.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressDBMapperTest {

    @Test
    fun `if min progress is set, then min gain in dB are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val gain = progressDbMapper.getGain(0)
        assertTrue(-12 == gain)
    }

    @Test
    fun `if max progress is set, then max gain in dB are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val gain = progressDbMapper.getGain(100)
        assertTrue(12 == gain)
    }

    @Test
    fun `if half progress is set, then half gain in dB are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val gain = progressDbMapper.getGain(50)
        assertTrue(0 == gain)
    }

    @Test
    fun `if quarter progress is set, then quarter gain in dB are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val gain = progressDbMapper.getGain(25)
        assertTrue(-6 == gain)
    }

    @Test
    fun `if half quarter progress is set, then half quarter gain in dB are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val gain = progressDbMapper.getGain(12)
        assertTrue(-9 == gain)
    }

    @Test
    fun `if min gain in dB is set, then min progress are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val progress = progressDbMapper.getProgress(-12)
        assertTrue(0 == progress)
    }

    @Test
    fun `if max gain in dB is set, then max progress are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val progress = progressDbMapper.getProgress(12)
        assertTrue(100 == progress)
    }

    @Test
    fun `if half gain in dB is set, then half progress are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val progress = progressDbMapper.getProgress(0)
        assertTrue(50 == progress)
    }

    @Test
    fun `if quarter gain in dB is set, then quarter progress are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val progress = progressDbMapper.getProgress(-6)
        assertTrue(25 == progress)
    }

    @Test
    fun `if half quarter gain in dB is set, then half quarter progress are returned`() {
        val progressDbMapper = buildProgressDbMapper()
        val progress = progressDbMapper.getProgress(-9)
        assertTrue(12 == progress)
    }

    private fun buildProgressDbMapper(): ProgressDbMapper {
        val gainMin = -12
        val gainMax = 12
        val progressMin = 0
        val progressMax = 100
        return ProgressDbMapper(gainMin, gainMax, progressMin, progressMax)
    }
}
