package soy.gabimoreno.audioclean.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import soy.gabimoreno.audioclean.domain.AudioProcessor
import soy.gabimoreno.audioclean.domain.MediaPlayer
import soy.gabimoreno.audioclean.domain.usecase.GetActiveRecordingConfigurationsUseCase
import soy.gabimoreno.audioclean.domain.usecase.GetAudioSessionIdUseCase
import soy.gabimoreno.audioclean.presentation.MainActivity
import soy.gabimoreno.audioclean.presentation.MainViewModel

val appModule = module {
    single { MediaPlayer(context = androidContext()) }
    single { GetActiveRecordingConfigurationsUseCase(context = androidContext()) }
    single { GetAudioSessionIdUseCase(getActiveRecordingConfigurationsUseCase = get()) }
    single { AudioProcessor(getAudioSessionIdUseCase = get()) }
    scope(named<MainActivity>()) {
        viewModel { (resId: Int) ->
            MainViewModel(
                mediaPlayer = get(),
                audioProcessor = get(),
                resId = resId
            )
        }
    }
}
