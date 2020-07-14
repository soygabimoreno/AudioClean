package soy.gabimoreno.audioclean.presentation

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.core.parameter.parametersOf
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.framework.AudioSessionReceiver
import soy.gabimoreno.audioclean.framework.KLog
import soy.gabimoreno.audioclean.presentation.customview.fader.Fader
import soy.gabimoreno.audioclean.presentation.customview.fader.FaderView
import org.koin.androidx.scope.lifecycleScope as koinScope

class MainActivity : AppCompatActivity() {

    private lateinit var audioSessionReceiver: AudioSessionReceiver

    @RawRes
    private val resId = R.raw.audio_demo

    private val viewModel: MainViewModel by koinScope.viewModel(this) { parametersOf(resId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBtnReset()
        initBtnPlayer()
        initCbAudioProcessor()
        initFaders()

        requireNotificationAccessPermission()

        audioSessionReceiver = AudioSessionReceiver()
        val intentFilter = IntentFilter()
        LocalBroadcastManager.getInstance(this).registerReceiver(audioSessionReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(audioSessionReceiver)
    }

    private fun requireNotificationAccessPermission() {
        if (Settings.Secure.getString(
                contentResolver,
                "enabled_notification_listeners"
            ).contains(applicationContext.packageName)
        ) {
            KLog.i("ACTION_NOTIFICATION_LISTENER was already enabled")
        } else {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        }
    }

    private fun initBtnReset() {
        btnReset.setOnClickListener {
            viewModel.resetFaders()
        }
    }

    private fun initBtnPlayer() {
        btnPlayer.setOnClickListener {
            if (viewModel.isPlayingAudio()) {
                btnPlayer.setText(R.string.start)
                viewModel.stopAudio()
            } else {
                btnPlayer.setText(R.string.stop)
                viewModel.playAudio()
            }
        }
    }

    private fun initCbAudioProcessor() {
        cbCleanAudio.setOnClickListener {
            if (cbCleanAudio.isChecked) {
                viewModel.startProcessing()
            } else {
                viewModel.stopProcessing()
            }
        }
    }

    private fun initFaders() {
        val frequencies = viewModel.getFrequencies()
        frequencies.forEachIndexed { i, item ->
            val faderView = FaderView(this)
            llFaderViews.addView(faderView)
            val fader = Fader(faderView, i, item)
            viewModel.addFader(fader)
        }
    }
}
