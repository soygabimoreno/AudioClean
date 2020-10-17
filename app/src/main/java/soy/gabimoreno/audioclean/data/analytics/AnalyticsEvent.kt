package soy.gabimoreno.audioclean.data.analytics

interface AnalyticsEvent {
    val name: String
    val parameters: Map<String, Any>
}
