package soy.gabimoreno.audioclean.domain

import android.media.audiofx.DynamicsProcessing
import soy.gabimoreno.audioclean.framework.KLog

class AudioProcessor(private val mediaPlayer: MediaPlayer) {

    companion object {
        private const val PRIORITY = Int.MAX_VALUE
        private const val VARIANT = 0
        private const val CHANNEL_COUNT = 1
        private const val PRE_EQ_IN_USE = true
        private const val MBC_IN_USE = true
        private const val POST_EQ_IN_USE = true
        private const val LIMITER_IN_USE = true
        private val BAND_TONES = intArrayOf(31, 62, 125, 250, 500, 1000, 2000, 4000, 8000, 16000)
        private val N_BANDS = BAND_TONES.size
        private val PRE_EQ_BAND_COUNT = N_BANDS
        private val MBC_BAND_COUNT = N_BANDS
        private val POST_EQ_BAND_COUNT = N_BANDS
    }

    private lateinit var dynamicsProcessing: DynamicsProcessing

    fun init() {
        val builder = DynamicsProcessing.Config.Builder(
            VARIANT,
            CHANNEL_COUNT,
            PRE_EQ_IN_USE,
            PRE_EQ_BAND_COUNT,
            MBC_IN_USE,
            MBC_BAND_COUNT,
            POST_EQ_IN_USE,
            POST_EQ_BAND_COUNT,
            LIMITER_IN_USE
        )
        val sessionId = mediaPlayer.sessionId
        dynamicsProcessing = DynamicsProcessing(PRIORITY, sessionId, builder.build())
    }

    fun start() {
        KLog.d("Starting Audio Processor...")
    }
}
