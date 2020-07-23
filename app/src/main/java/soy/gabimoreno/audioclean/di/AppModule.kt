package soy.gabimoreno.audioclean.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import soy.gabimoreno.audioclean.domain.usecase.GetAudioSessionIdUseCase
import soy.gabimoreno.audioclean.framework.AudioProcessor
import soy.gabimoreno.audioclean.framework.GetActiveRecordingConfigurations
import soy.gabimoreno.audioclean.framework.MediaPlayer
import soy.gabimoreno.audioclean.presentation.MainActivity
import soy.gabimoreno.audioclean.presentation.MainViewModel

val appModule = module {
    single { MediaPlayer(context = androidContext()) }
    single { GetActiveRecordingConfigurations(context = androidContext()) }
    single { GetAudioSessionIdUseCase(getActiveRecordingConfigurations = get()) }
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
