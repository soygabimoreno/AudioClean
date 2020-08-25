package soy.gabimoreno.audioclean.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.scope.viewModel
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.framework.extension.isFilled
import soy.gabimoreno.audioclean.framework.extension.setOnItemSelected
import soy.gabimoreno.audioclean.presentation.customview.fader.Fader
import soy.gabimoreno.audioclean.presentation.customview.fader.FaderView
import soy.gabimoreno.audioclean.service.AudioProcessorService
import java.text.SimpleDateFormat
import java.util.*
import org.koin.androidx.scope.lifecycleScope as koinScope

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by koinScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        initSpinner()
        initBtnSave()
        initBtnReset()
        initCbAudioProcessor()
        initFaders()
    }

    private fun initViewModel() {
        viewModel.info.observe(this, Observer { info ->
            tvInfo.text = "Audio Session Id: $info"
        })

        viewModel.equalization.observe(this, Observer { equalization ->
            AlertDialog.Builder(
                this@MainActivity
            )
                .setTitle(R.string.equalization)
                .setMessage(equalization)
                .show()
        })

        viewModel.equalizationNames.observe(this, Observer { names ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, names)
            spinner.adapter = adapter
        })
    }

    private fun initSpinner() {
        spinner.setOnItemSelected { position ->
            viewModel.loadEqualization(position)
        }
    }

    private fun initBtnSave() {
        btnSave.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle(R.string.equalization)
            val et = EditText(this)

            val pattern = "yyyy-MM-dd HH:mm:ss"
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
            val date = simpleDateFormat.format(Date())
            et.setText(date.toString())

            builder.setView(et)
            builder.setPositiveButton(R.string.save) { _, _ ->
                val equalizationName = et.text.toString()
                if (equalizationName.isFilled()) {
                    viewModel.saveEqualization(equalizationName)
                }
            }
            builder.show()
        }
    }

    private fun initBtnReset() {
        btnReset.setOnClickListener {
            viewModel.resetFaders()
        }
    }

    private fun initCbAudioProcessor() {
        cbCleanAudio.setOnClickListener {
            if (cbCleanAudio.isChecked) {
//                viewModel.startProcessing()

                val intent = Intent(this, AudioProcessorService::class.java)
                intent.putExtra(AudioProcessorService.EXTRA_TEXT, "AudioClean Text")
                startForegroundService(intent)
            } else {
//                viewModel.stopProcessing()

                val intent = Intent(this, AudioProcessorService::class.java)
                stopService(intent)
            }
        }
    }

    private fun initFaders() {
        val frequencies = viewModel.getFrequencies()
        val gains = viewModel.getGains()
        frequencies.forEachIndexed { i, _ ->
            val faderView = FaderView(this)
            llFaderViews.addView(faderView)
            val fader = Fader(faderView, i, frequencies[i], gains[i])
            viewModel.addFader(fader)
        }
    }
}
