package soy.gabimoreno.audioclean.presenter

import android.os.Bundle
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.core.parameter.parametersOf
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.presenter.customview.fader.Fader
import soy.gabimoreno.audioclean.presenter.customview.fader.FaderView
import org.koin.androidx.scope.lifecycleScope as koinScope

class MainActivity : AppCompatActivity() {

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
