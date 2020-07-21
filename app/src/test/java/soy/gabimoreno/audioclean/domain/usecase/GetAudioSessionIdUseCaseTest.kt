package soy.gabimoreno.audioclean.domain.usecase

import android.media.AudioRecordingConfiguration
import arrow.core.Either
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

class GetAudioSessionIdUseCaseTest {

    private val getActiveRecordingConfigurationsUseCase = mockk<GetActiveRecordingConfigurationsUseCase>()
    private val audioRecordingConfiguration = mockk<AudioRecordingConfiguration>()
    private val audioRecordingConfigurations = mockk<List<AudioRecordingConfiguration>>()

    @Test
    fun `if there is no active session, then returns then corresponding error`() {
        givenNoActiveSessions()
        val useCase = buildGetAudioSessionIdUseCase()

        val audioSessionId = useCase()

        assertTrue(GetAudioSessionIdUseCase.Error.NoActiveRecordingConfiguration.type == audioSessionId)
    }

    @Test
    fun `if no audioSession is found in any of the active sessions, then returns the corresponding error`() {
        givenActiveSessions()
        every { audioRecordingConfiguration.clientAudioSessionId } returns GetAudioSessionIdUseCase.Error.NoAudioSessionId.type
        val useCase = buildGetAudioSessionIdUseCase()

        val audioSessionId = useCase()

        assertTrue(GetAudioSessionIdUseCase.Error.NoAudioSessionId.type == audioSessionId)
    }

    @Test
    fun `if audioSession is found in any of the active sessions, then returns the audioSessionId`() {
        givenActiveSessions()
        val fakeAudioSessionId = 1234
        val fakeAudioRecordingSessionId = 1234 + GetAudioSessionIdUseCase.AUDIO_SESSION_ID_STEP
        every { audioRecordingConfiguration.clientAudioSessionId } returns fakeAudioRecordingSessionId
        val useCase = buildGetAudioSessionIdUseCase()

        val audioSessionId = useCase()

        assertTrue(fakeAudioSessionId == audioSessionId)
    }

    private fun givenNoActiveSessions() {
        every { getActiveRecordingConfigurationsUseCase() } returns
            Either.left(GetActiveRecordingConfigurationsUseCase.NoActiveRecordingConfigurationException)
    }

    private fun givenActiveSessions() {
        every { audioRecordingConfigurations[0] } returns audioRecordingConfiguration
        every { getActiveRecordingConfigurationsUseCase() } returns Either.right(audioRecordingConfigurations)
    }

    private fun buildGetAudioSessionIdUseCase() =
        GetAudioSessionIdUseCase(getActiveRecordingConfigurationsUseCase)
}
