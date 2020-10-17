package soy.gabimoreno.audioclean.data.analytics

import com.amplitude.api.Amplitude
import org.koin.dsl.module

val analyticsTrackerModule = module {
    single { Amplitude.getInstance() }

    single<AnalyticsTrackerComponent> {
        AnalyticsTracker(
            listOf(
                AmplitudeAnalyticsTrackerComponent(
                    amplitudeClient = get()
                )
            )
        )
    }
}
