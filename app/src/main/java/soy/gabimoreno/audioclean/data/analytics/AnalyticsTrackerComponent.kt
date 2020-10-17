package soy.gabimoreno.audioclean.data.analytics

interface AnalyticsTrackerComponent {
    fun <E : AnalyticsEvent> trackEvent(event: E)
}
