package soy.gabimoreno.audioclean.domain

import android.media.audiofx.DynamicsProcessing
import android.media.audiofx.DynamicsProcessing.*
import soy.gabimoreno.audioclean.framework.KLog

class AudioProcessor(private val mediaPlayer: MediaPlayer) {

    companion object {
        private const val PRIORITY = Int.MAX_VALUE
        private const val VARIANT = 0
        private const val CHANNEL_COUNT = 1
        private const val PRE_EQ_IN_USE = true
        private const val MULTI_BAND_COMPRESSOR_IN_USE = true
        private const val POST_EQ_IN_USE = true
        private const val LIMITER_IN_USE = true

        private val FREQUENCIES = intArrayOf(31, 62, 125, 250, 500, 1000, 2000, 4000, 8000, 16000)
        private val N_BANDS = FREQUENCIES.size

        private val PRE_EQ_BAND_COUNT = N_BANDS
        private val MULTI_BAND_COMPRESSOR_BAND_COUNT = N_BANDS
        private val POST_EQ_BAND_COUNT = N_BANDS

        private const val LIMITER_ENABLED = true
        private const val LIMITER_LINK_GROUP = 0
        private const val LIMITER_ATTACK_TIME_MS = 1f
        private const val LIMITER_RELEASE_TIME_MS = 60f
        private const val LIMITER_RATIO = 10f // N:1
        private const val LIMITER_THRESHOLD_DB = -2f
        private const val LIMITER_POST_GAIN_DB = 0f
    }

    private lateinit var dynamicsProcessing: DynamicsProcessing
    private lateinit var eq: Eq
    private lateinit var mbc: Mbc
    private lateinit var limiter: Limiter

    //    private val eqValues = IntArray(N_BANDS)
    private val eqValues = intArrayOf(0, 0, 0, 0, 0, 0, 0, 6, 6, 6)
    private lateinit var frequencies: IntArray

    fun init() {
        val builder = Config.Builder(
            VARIANT,
            CHANNEL_COUNT,
            PRE_EQ_IN_USE,
            PRE_EQ_BAND_COUNT,
            MULTI_BAND_COMPRESSOR_IN_USE,
            MULTI_BAND_COMPRESSOR_BAND_COUNT,
            POST_EQ_IN_USE,
            POST_EQ_BAND_COUNT,
            LIMITER_IN_USE
        )
        val sessionId = mediaPlayer.sessionId
        dynamicsProcessing = DynamicsProcessing(
            PRIORITY,
            sessionId,
            builder.build()
        )
        dynamicsProcessing.enabled = true


        frequencies = FREQUENCIES // TODO: This is temporary. Get the proper tones each device should have

        eq = Eq(true, true, N_BANDS)
        eq.isEnabled = true

        mbc = Mbc(true, true, N_BANDS)
        mbc.isEnabled = true

        limiter = Limiter(
            LIMITER_IN_USE,
            LIMITER_ENABLED,
            LIMITER_LINK_GROUP,
            LIMITER_ATTACK_TIME_MS,
            LIMITER_RELEASE_TIME_MS,
            LIMITER_RATIO,
            LIMITER_THRESHOLD_DB,
            LIMITER_POST_GAIN_DB
        )
        limiter.isEnabled = true
    }

    fun start() {
        init()
        KLog.d("Starting Audio Processor...")
        for (i in 0 until N_BANDS) {
            eq.getBand(i).cutoffFrequency = frequencies[i].toFloat()
            setBandGain(i, eqValues[i])
            mbc.getBand(i).cutoffFrequency = frequencies[i].toFloat()
        }
        dynamicsProcessing.setPreEqAllChannelsTo(eq)
        dynamicsProcessing.setMbcAllChannelsTo(mbc)
        dynamicsProcessing.setPostEqAllChannelsTo(eq)
        dynamicsProcessing.setLimiterAllChannelsTo(limiter)
    }

    fun stop() {
        dynamicsProcessing.enabled = false
    }

    private fun setBandGain(position: Int, level: Int) {
        eqValues[position] = level
        val band = eq.getBand(position)
        band.isEnabled = true
        band.gain = eqValues[position].toFloat()
        dynamicsProcessing.setPreEqBandAllChannelsTo(position, band)
        dynamicsProcessing.setPostEqBandAllChannelsTo(position, band)
    }

    fun getFrequencies(): IntArray {
        return frequencies
    }

    fun setVolume(i: Int, gain: Int) {
        setBandGain(i, gain)
    }
}
