package soy.gabimoreno.audioclean.domain.usecase

class GetAudioSessionIdUseCase(
    private val getActiveRecordingConfigurationsUseCase: GetActiveRecordingConfigurationsUseCase
) {

    companion object {
        const val AUDIO_SESSION_ID_STEP = 8
    }

    sealed class Error(val type: Int) {
        object NoActiveRecordingConfiguration : Error(-1)
        object NoAudioSessionId : Error(-2)
    }

    operator fun invoke(): Int {
        getActiveRecordingConfigurationsUseCase()
            .fold({
                return Error.NoActiveRecordingConfiguration.type
            }, { activeRecordingConfigurations ->
                val audioSessionId = activeRecordingConfigurations[0].clientAudioSessionId - AUDIO_SESSION_ID_STEP
                return if (audioSessionId < 0) {
                    Error.NoAudioSessionId.type
                } else {
                    audioSessionId
                }
            })
    }
}
