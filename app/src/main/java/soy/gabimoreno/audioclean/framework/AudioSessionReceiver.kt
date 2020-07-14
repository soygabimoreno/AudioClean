package soy.gabimoreno.audioclean.framework

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.audiofx.Equalizer

class AudioSessionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val audioSessionId = intent.getIntExtra(Equalizer.EXTRA_AUDIO_SESSION, -1)
            val packageName = intent.getStringExtra(Equalizer.EXTRA_PACKAGE_NAME)
            KLog.i("audioSessionId: $audioSessionId")
            KLog.i("packageName: $packageName")
        } ?: KLog.w("Intent is null")
    }
}

