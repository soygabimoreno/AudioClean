package soy.gabimoreno.audioclean.presentation

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import soy.gabimoreno.audioclean.data.preferences.EqualizationDatasource
import soy.gabimoreno.audioclean.domain.usecase.GetEqualizationUseCase
import soy.gabimoreno.audioclean.framework.AudioProcessor
import soy.gabimoreno.audioclean.framework.MediaPlayer

class MainViewModelTest {

    private val mediaPlayer = mockk<MediaPlayer>(relaxed = true)
    private val audioProcessor = mockk<AudioProcessor>(relaxed = true)
    private val equalizationDatasource = mockk<EqualizationDatasource>(relaxed = true)
    private val getEqualizationUseCase = mockk<GetEqualizationUseCase>(relaxed = true)
    private val resId = 1234

    @Test
    fun `if ViewModel is created, then MediaPlayer is called once`() {
        buildViewModel()
        verify(exactly = 1) { mediaPlayer.init(resId) }
    }

    @Test
    fun `if ViewModel is created, then AudioProcessor is called once`() {
        buildViewModel()
        verify(exactly = 1) { audioProcessor.init() }
    }

    @Test
    fun `if playAudio is called, then MediaPlayer is initialized twice and started only once`() {
        val viewModel = buildViewModel()
        viewModel.playAudio()
        verify(exactly = 2) { mediaPlayer.init(resId) }
        verify(exactly = 1) { mediaPlayer.start() }
    }

    private fun buildViewModel(): MainViewModel {
        return MainViewModel(
            mediaPlayer,
            audioProcessor,
            equalizationDatasource,
            getEqualizationUseCase,
            resId
        )
    }
}
