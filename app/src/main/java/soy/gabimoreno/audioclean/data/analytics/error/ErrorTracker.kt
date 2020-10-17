package soy.gabimoreno.audioclean.data.analytics.error

class ErrorTracker(
    private val components: List<ErrorTrackerComponent>
) : ErrorTrackerComponent {
    override fun <E : ErrorEvent> trackError(event: E) {
        components.forEach { it.trackError(event) }
    }
}
