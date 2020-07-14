package soy.gabimoreno.audioclean.domain.usecase

import android.media.session.MediaController
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

class GetAudioSessionIdUseCaseTest {

    private val activeSessionsUseCase = mockk<GetActiveSessionsUseCase>()
    private val mediaController = mockk<MediaController>()
    private val mediaControllers = mockk<List<MediaController>>()

    @Test
    fun `if there is no active session, then returns then corresponding error`() {
        givenNoActiveSessions()
        val useCase = buildGetAudioSessionIdUseCase()

        val audioSessionId = useCase()

        assertTrue(GetAudioSessionIdUseCase.Error.NoActiveSession.type == audioSessionId)
    }

    @Test
    fun `if no audioSession is found in any of the active sessions, then returns the corresponding error`() {
        givenActiveSessions()
        val useCase = buildGetAudioSessionIdUseCase()

        val audioSessionId = useCase()

        assertTrue(GetAudioSessionIdUseCase.Error.NotFoundSessionId.type == audioSessionId)
    }

    private fun givenNoActiveSessions() {
        every { activeSessionsUseCase() } returns emptyList()
    }

    private fun givenActiveSessions() {
        every { mediaControllers[0] } returns mediaController
        every { activeSessionsUseCase() } returns mediaControllers
    }

    private fun buildGetAudioSessionIdUseCase() =
        GetAudioSessionIdUseCase(activeSessionsUseCase)
}
