package soy.gabimoreno.audioclean.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
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
        const val EXTRA_TEXT = "EXTRA_TEXT"
    }

    private val audioProcessor: AudioProcessor by inject()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val text = intent?.getStringExtra(EXTRA_TEXT) ?: "Unknown Text"

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            0
        )
        val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
            .setContentTitle("AudioClean Service")
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
