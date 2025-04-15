package soy.gabimoreno.audioclean.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.right
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import soy.gabimoreno.audioclean.data.analytics.AnalyticsTrackerComponent
import soy.gabimoreno.audioclean.data.analytics.error.ErrorTrackerComponent
import soy.gabimoreno.audioclean.data.preferences.EqualizationDatasource
import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.domain.FrequencyGain
import soy.gabimoreno.audioclean.domain.usecase.GetAudioSessionIdUseCase
import soy.gabimoreno.audioclean.domain.usecase.GetEqualizationUseCase
import soy.gabimoreno.audioclean.framework.AudioProcessor
import soy.gabimoreno.audioclean.infrastructure.InfrastructureKeys
import soy.gabimoreno.audioclean.presentation.analytics.MainEvents

@ExperimentalCoroutinesApi
class MainViewModelTest {

    companion object {
        private const val EQUALIZATION_NAME = "Equalization Name"
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val audioProcessor = mockk<AudioProcessor>(relaxed = true)
    private val equalizationDatasource = mockk<EqualizationDatasource>(relaxed = true)
    private val getEqualizationUseCase = mockk<GetEqualizationUseCase>(relaxed = true)
    private val analyticsTrackerComponent = mockk<AnalyticsTrackerComponent>(relaxed = true)
    private val errorTrackerComponent = mockk<ErrorTrackerComponent>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        givenAllEqualizations()
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `if ViewModel is created, then AudioProcessor is called once`() {
        buildViewModel()
        verify(exactly = 1) { audioProcessor.init() }
    }

    @Test
    fun `if releaseProcessing is called, then audioProcessor release is triggered`() {
        val viewModel = buildViewModel()

        viewModel.releaseProcessing()

        verify(exactly = 1) { audioProcessor.release() }
    }

    @Test
    fun `when viewModel is initialized, if there is no available audio session, it is triggered by the analytics tracker`() {
        buildViewModelWithBuiltAudioProcessor()

        val slot = slot<MainEvents.DataAudioSession>()
        verify(exactly = 1) { analyticsTrackerComponent.trackEvent(capture(slot)) }
        assertTrue(-1 == slot.captured.parameters[InfrastructureKeys.AUDIO_SESSION_ID])
    }

    @Test
    fun `when an equalization is saved, then its analytics event is triggered`() {
        givenEqualization()
        val viewModel = buildViewModel()

        val equalizationName = EQUALIZATION_NAME
        viewModel.onSaveEqualizationClicked(equalizationName)

        val slot = slot<MainEvents.ClickSaveEqualization>()
        verify(exactly = 1) { analyticsTrackerComponent.trackEvent(capture(slot)) }
        assertTrue(slot.captured.parameters[InfrastructureKeys.EQUALIZATION].toString().contains(equalizationName))
    }

    private fun givenAllEqualizations() {
        every { equalizationDatasource.loadAll() } returns mutableListOf(buildEqualization()).right()
    }

    private fun givenEqualization() {
        every { getEqualizationUseCase(any(), any(), any()) } returns buildEqualization()
    }

    private fun buildEqualization() = Equalization(
        name = EQUALIZATION_NAME,
        listOf(
            FrequencyGain(31, 0),
            FrequencyGain(62, 0),
            FrequencyGain(125, 0),
            FrequencyGain(250, 0),
            FrequencyGain(500, 0),
            FrequencyGain(1000, 0),
            FrequencyGain(2000, 0),
            FrequencyGain(4000, 0),
            FrequencyGain(8000, 0),
            FrequencyGain(16000, 0)
        )
    )

    private fun buildViewModel(): MainViewModel {
        return MainViewModel(
            audioProcessor,
            equalizationDatasource,
            getEqualizationUseCase,
            analyticsTrackerComponent,
            errorTrackerComponent
        )
    }

    private fun buildViewModelWithBuiltAudioProcessor(): MainViewModel {
        return MainViewModel(
            buildAudioProcessor(),
            equalizationDatasource,
            getEqualizationUseCase,
            analyticsTrackerComponent,
            errorTrackerComponent
        )
    }

    private fun buildAudioProcessor(): AudioProcessor {
        val getAudioSessionIdUseCase = mockk<GetAudioSessionIdUseCase>(relaxed = true)
        val equalization = mockk<Equalization>(relaxed = true)

        every { getAudioSessionIdUseCase() } returns -1
        return AudioProcessor(
            getAudioSessionIdUseCase,
            equalization
        )
    }
}
