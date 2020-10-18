package soy.gabimoreno.audioclean.presentation.analytics

import soy.gabimoreno.audioclean.data.analytics.AnalyticsEvent

private const val SCREEN_MAIN = "SCREEN_MAIN"
private const val DATA_AUDIO_SESSION = "DATA_AUDIO_SESSION"
private const val CLICK_SAVE_EQUALIZATION = "CLICK_SAVE_EQUALIZATION"

private const val AUDIO_SESSION_ID = "AUDIO_SESSION_ID"

sealed class MainEvents(
    override val name: String,
    override val parameters: Map<String, Any> = mapOf()
) : AnalyticsEvent {

    object ScreenMain : MainEvents(SCREEN_MAIN)

    class DataAudioSession(
        audioSessionId: Int
    ) : MainEvents(
        DATA_AUDIO_SESSION,
        mapOf(
            AUDIO_SESSION_ID to audioSessionId
        )
    )
}
