package soy.gabimoreno.audioclean.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.right
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import soy.gabimoreno.audioclean.data.analytics.AnalyticsTrackerComponent
import soy.gabimoreno.audioclean.data.analytics.error.ErrorTrackerComponent
import soy.gabimoreno.audioclean.data.preferences.EqualizationDatasource
import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.domain.usecase.GetEqualizationUseCase
import soy.gabimoreno.audioclean.framework.AudioProcessor

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    private val audioProcessor = mockk<AudioProcessor>(relaxed = true)
    private val equalizationDatasource = mockk<EqualizationDatasource>(relaxed = true)
    private val getEqualizationUseCase = mockk<GetEqualizationUseCase>(relaxed = true)
    private val analyticsTrackerComponent = mockk<AnalyticsTrackerComponent>(relaxed = true)
    private val errorTrackerComponent = mockk<ErrorTrackerComponent>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun `if ViewModel is created, then AudioProcessor is called once`() {
        givenAllEqualizations()
        buildViewModel()
        verify(exactly = 1) { audioProcessor.init() }
    }

    @Test
    fun `if releaseProcessing is called, then audioProcessor release is triggered`() {
        givenAllEqualizations()
        val viewModel = buildViewModel()

        viewModel.releaseProcessing()

        verify(exactly = 1) { audioProcessor.release() }
    }

    private fun buildViewModel(): MainViewModel {
        return MainViewModel(
            audioProcessor,
            equalizationDatasource,
            getEqualizationUseCase,
            analyticsTrackerComponent,
            errorTrackerComponent
        )
    }

    private fun givenAllEqualizations() {
        every { equalizationDatasource.loadAll() } returns listOf<Equalization>().right()
    }
}
