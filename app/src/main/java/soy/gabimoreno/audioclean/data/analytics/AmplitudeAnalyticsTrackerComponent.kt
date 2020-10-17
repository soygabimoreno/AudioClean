package soy.gabimoreno.audioclean.data.analytics

import com.amplitude.api.AmplitudeClient
import soy.gabimoreno.audioclean.BuildConfig
import soy.gabimoreno.audioclean.framework.KLog
import soy.gabimoreno.audioclean.framework.extension.toJSONObject

class AmplitudeAnalyticsTrackerComponent(
    private val amplitudeClient: AmplitudeClient
) : AnalyticsTrackerComponent {
    override fun <E : AnalyticsEvent> trackEvent(event: E) {
        val commonParameters = mapOf(
            "BUILD_TYPE" to BuildConfig.BUILD_TYPE
        )
        val parameters = event.parameters + commonParameters
        amplitudeClient.logEvent(event.name, parameters.toJSONObject())
        KLog.i("${event.name}: $parameters")
    }
}
