package soy.gabimoreno.audioclean.presenter

import android.os.Bundle
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.core.parameter.parametersOf
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.presenter.customview.fader.Fader
import org.koin.androidx.scope.lifecycleScope as koinScope

class MainActivity : AppCompatActivity() {

    @RawRes
    private val resId = R.raw.audio_demo

    private val viewModel: MainViewModel by koinScope.viewModel(this) { parametersOf(resId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBtnPlayer()
        initCbAudioProcessor()
        initFader()
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

    private fun initFader() {
        val frequencies = viewModel.getFrequencies()
        val fader0 = Fader(fv0, frequencies[0])
        fader0.setListener(object : Fader.Listener {
            override fun onGainChanged(gain: Int) {
                viewModel.setVolume(gain)
            }
        })
        val fader1 = Fader(fv1, frequencies[1])
        fader1.setListener(object : Fader.Listener {
            override fun onGainChanged(gain: Int) {
                viewModel.setVolume(gain)
            }
        })
    }
}
