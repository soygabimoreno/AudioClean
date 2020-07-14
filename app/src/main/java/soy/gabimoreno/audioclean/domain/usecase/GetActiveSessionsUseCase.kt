package soy.gabimoreno.audioclean.domain.usecase

import android.content.ComponentName
import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import soy.gabimoreno.audioclean.domain.NotificationListener

class GetActiveSessionsUseCase(
    private val context: Context
) {

    operator fun invoke(): List<MediaController> {
        val mediaSessionManager = (context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager)
        return mediaSessionManager.getActiveSessions(ComponentName(context, NotificationListener::class.java))
    }
}
