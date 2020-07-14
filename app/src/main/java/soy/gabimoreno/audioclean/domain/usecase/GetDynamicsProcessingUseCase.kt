package soy.gabimoreno.audioclean.domain.usecase

import android.media.audiofx.DynamicsProcessing
import soy.gabimoreno.audioclean.framework.KLog

class GetDynamicsProcessingUseCase(
    private val getAudioSessionIdUseCase: GetAudioSessionIdUseCase
) {

    companion object {
        private const val PRIORITY = Int.MAX_VALUE
    }

    operator fun invoke(builder: DynamicsProcessing.Config.Builder): DynamicsProcessing {
        val audioSessionId = getAudioSessionIdUseCase()
        try {
            val dynamicsProcessing = DynamicsProcessing(
                PRIORITY,
                audioSessionId,
                builder.build()
            )
            KLog.i("Active audioSessionId: $audioSessionId")
            return dynamicsProcessing
        } catch (e: Exception) {
            KLog.e("audioSessionId not valid: $audioSessionId")
        }
        throw Exception("AudioSessionId Not Found.")
    }
}
