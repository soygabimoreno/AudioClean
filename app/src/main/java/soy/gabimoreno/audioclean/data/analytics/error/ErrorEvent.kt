package soy.gabimoreno.audioclean.data.analytics.error

interface ErrorEvent {
    val parameters: Map<String, Any>
}

data class ThrowableErrorEvent(
    val throwable: Throwable,
    override val parameters: Map<String, Any> = emptyMap()
) : ErrorEvent

data class NonStandardErrorEvent(
    val tag: String,
    override val parameters: Map<String, Any> = emptyMap()
) : ErrorEvent
