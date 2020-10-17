package soy.gabimoreno.audioclean.presentation.analytics

import soy.gabimoreno.audioclean.data.analytics.AnalyticsEvent

private const val SCREEN_MAIN = "SCREEN_MAIN"

sealed class MainEvents(
    override val name: String,
    override val parameters: Map<String, Any> = mapOf()
) : AnalyticsEvent {

    object ScreenMain : MainEvents(SCREEN_MAIN)
}
