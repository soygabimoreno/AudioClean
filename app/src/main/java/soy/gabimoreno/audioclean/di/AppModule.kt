package soy.gabimoreno.audioclean.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import soy.gabimoreno.audioclean.data.DefaultUserSession
import soy.gabimoreno.audioclean.data.equalization.VoiceManEqualization
import soy.gabimoreno.audioclean.data.preferences.EqualizationDatasource
import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.domain.UserSession
import soy.gabimoreno.audioclean.domain.usecase.GetAudioSessionIdUseCase
import soy.gabimoreno.audioclean.domain.usecase.GetEqualizationUseCase
import soy.gabimoreno.audioclean.framework.AudioProcessor
import soy.gabimoreno.audioclean.framework.GetActiveRecordingConfigurations
import soy.gabimoreno.audioclean.presentation.MainActivity
import soy.gabimoreno.audioclean.presentation.MainViewModel

val appModule = module {
    single<UserSession> { DefaultUserSession() }
    single {
        GetActiveRecordingConfigurations(
            context = androidContext(),
            errorTrackerComponent = get()
        )
    }
    single { GetAudioSessionIdUseCase(getActiveRecordingConfigurations = get()) }
    single { GetEqualizationUseCase() }
    single<Equalization> { VoiceManEqualization().get() }
    single {
        EqualizationDatasource(
            sharedPreferences = androidContext()
                .getSharedPreferences("AUDIO_CLEAN_PREFS", Context.MODE_PRIVATE)
        )
    }
    single {
        AudioProcessor(
            getAudioSessionIdUseCase = get(),
            equalization = get()
        )
    }
    scope(named<MainActivity>()) {
        viewModel {
            MainViewModel(
                audioProcessor = get(),
                equalizationDatasource = get(),
                getEqualizationUseCase = get(),
                analyticsTrackerComponent = get(),
                errorTrackerComponent = get()
            )
        }
    }
}
