package soy.gabimoreno.audioclean.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import org.koin.android.ext.android.inject
import soy.gabimoreno.audioclean.App
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.framework.AudioProcessor
import soy.gabimoreno.audioclean.presentation.MainActivity

class AudioProcessorService : Service() {

    companion object {
        private const val EXTRA_TEXT = "EXTRA_TEXT"

        fun start(context: Context, text: String) {
            context.startForegroundService(
                Intent(context, AudioProcessorService::class.java).apply {
                    putExtra(EXTRA_TEXT, text)
                })
        }

        fun stop(context: Context) {
            context.stopService(
                Intent(context, AudioProcessorService::class.java)
            )
        }
    }

    private val audioProcessor: AudioProcessor by inject()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val text = intent?.getStringExtra(EXTRA_TEXT) ?: "Unknown Text"

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
            .setContentTitle(getString(R.string.processing_audio))
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_baseline_audiotrack_24)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        audioProcessor.start()

        // TODO: Here I have to process any effect

        return START_STICKY
    }

    override fun onDestroy() {
        audioProcessor.stop()
        super.onDestroy()
    }
}
