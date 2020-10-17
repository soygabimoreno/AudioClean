package soy.gabimoreno.audioclean.data.analytics.error

import com.google.firebase.crashlytics.FirebaseCrashlytics
import soy.gabimoreno.audioclean.domain.UserSession

class CrashlyticsErrorTrackerComponent(
    private val crashlytics: FirebaseCrashlytics,
    private val userSession: UserSession
) : ErrorTrackerComponent {

    override fun <E : ErrorEvent> trackError(event: E) {
        with(crashlytics) {
            val eventWithCommonAttributes = addCommonAttributes(event)

            eventWithCommonAttributes.parameters.forEach { (key, value) ->
                setCustomKey(key, value.toString())
            }

            when (event) {
                is NonStandardErrorEvent -> recordException(Exception(event.tag))
                is ThrowableErrorEvent -> recordException(event.throwable)
            }
        }
    }

    private fun <E : ErrorEvent> addCommonAttributes(event: E): ErrorEvent {
        val foo = userSession.foo()
        val commonParameters = mapOf(
            "foo" to foo
        )
        val parameters = event.parameters + commonParameters
        return when (event) {
            is NonStandardErrorEvent -> event.copy(parameters = parameters)
            is ThrowableErrorEvent -> event.copy(parameters = parameters)
            else -> object : ErrorEvent {
                override val parameters: Map<String, Any> = parameters
            }
        }
    }
}
