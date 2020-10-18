package soy.gabimoreno.audioclean.presentation

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.scope.viewModel
import soy.gabimoreno.audioclean.BuildConfig
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.data.preferences.EqualizationDatasource
import soy.gabimoreno.audioclean.framework.extension.*
import soy.gabimoreno.audioclean.presentation.customview.fader.Fader
import soy.gabimoreno.audioclean.presentation.customview.fader.FaderView
import soy.gabimoreno.audioclean.service.AudioProcessorService
import java.text.SimpleDateFormat
import java.util.*
import org.koin.androidx.scope.lifecycleScope as koinScope

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by koinScope.viewModel(this)

    @Deprecated("This is a patch for not showing the dialog the first time")
    private var showDialogToTheUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        initSpinner()
        initBtnSave()
        initBtnPresets()
        initBtnDeleteAll()
        initBtnReset()
        initTvDebugInfo()
        initSwitchFilter()
        initFaders()
    }

    override fun onDestroy() {
        viewModel.releaseProcessing()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuShare -> {
//                viewModel.handleShareClicked()
                share()
                true
            }
            R.id.menuEmail -> {
//                viewModel.handleEmailClicked()
                sendEmail()
                true
            }
            R.id.menuRate -> {
//                viewModel.handleRateClicked()
                rate()
                true
            }
            R.id.menuInfo -> {
//                viewModel.handleInfoClicked()
                navigateToWeb("http://appacoustic.com")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun share() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_title))
        startActivity(shareIntent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_to)))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
        startActivity(Intent.createChooser(intent, getString(R.string.email_title)))
    }

    private fun rate() {
        val appPackageName = if (BuildConfig.DEBUG) "soy.gabimoreno.audioclean" else packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (exception: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    private fun navigateToWeb(uriString: String) {
        CustomTabsIntent.Builder()
            .setStartAnimations(
                this,
                R.anim.browser_in_right,
                R.anim.browser_out_left
            )
            .setExitAnimations(
                this,
                R.anim.browser_in_left,
                R.anim.browser_out_right
            )
            .build()
            .launchUrl(
                this,
                Uri.parse(uriString)
            )
    }

    private fun initViewModel() {
        viewModel.info.observe(this, { info ->
            tvDebugInfo.text = "Audio Session Id: $info"

            if (info == "-1") {
                AlertDialog.Builder(
                    this@MainActivity
                )
                    .setTitle(R.string.disconnected_dialog_title)
                    .setMessage(R.string.disconnected_dialog_message)
                    .show()
                switchFilter.disable()
                tvConnected.setBackgroundResource(R.drawable.bg_tv_disconnected)
                tvConnected.setText(R.string.disconnected)
                tvConnected.typeface = Typeface.DEFAULT
                tvConnected.setTextColor(resources.getColor(R.color.grayDark, null))
            } else {
                switchFilter.enable()
                tvConnected.setBackgroundResource(R.drawable.bg_tv_connected)
                tvConnected.setText(R.string.connected)
                tvConnected.typeface = Typeface.DEFAULT_BOLD
                tvConnected.setTextColor(resources.getColor(R.color.accent, null))
            }
        })

        viewModel.equalizations.observe(this, { equalizations ->
            val currentEqualizationPosition = viewModel.currentEqualizationPosition.value!!
            if (equalizations.isNotEmpty()) {
                if (showDialogToTheUser) {
                    AlertDialog.Builder(
                        this@MainActivity
                    )
                        .setTitle(R.string.equalization)
                        .setMessage(equalizations[currentEqualizationPosition].toString())
                        .show()
                }
                showDialogToTheUser = true
            }

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                equalizations.map { it.name }
            )
            spinner.adapter = adapter

            spinner.setSelection(currentEqualizationPosition)
        })

        viewModel.currentEqualizationPosition.observe(this, { currentEqualizationPosition ->
            spinner.setSelection(currentEqualizationPosition)
        })
    }

    private fun initSpinner() {
        spinner.setOnItemSelected { position ->
            viewModel.loadEqualization(position)
        }
    }

    private fun initBtnSave() {
        btnSave.setOnClickListener {
            if (viewModel.getNumberOfEqualizations() < EqualizationDatasource.Positions.values().size) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle(R.string.equalization)

                val pattern = "yyyy-MM-dd HH:mm:ss"
                val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                val date = simpleDateFormat.format(Date())

                val et = EditText(this)
                et.setText(date.toString())
                builder.setView(et)

                builder.setPositiveButton(R.string.save) { _, _ ->
                    val equalizationName = et.text.toString()
                    if (equalizationName.isFilled()) {
                        viewModel.onSaveEqualizationClicked(equalizationName)
                    }
                }
                builder.show()
            } else {
                toast(R.string.max_number_of_saved_equalizations_reached)
            }
        }
    }

    private fun initBtnPresets() {
        btnPreset1.setOnClickListener {
            viewModel.onBtnPreset1clicked()
        }
        btnPreset2.setOnClickListener {
            viewModel.onBtnPreset2clicked()
        }
        btnPreset3.setOnClickListener {
            viewModel.onBtnPreset3clicked()
        }
    }

    private fun initBtnDeleteAll() {
        btnDeleteAll.setOnClickListener {
            viewModel.onDeleteAllClicked()
        }
    }

    private fun initBtnReset() {
        btnReset.setOnClickListener {
            viewModel.resetFaders()
        }
    }

    private fun initTvDebugInfo() {
        tvDebugInfo.setVisibleOrGone(BuildConfig.DEBUG)
    }

    private fun initSwitchFilter() {
        switchFilter.setOnCheckedChangeListener { _, filterEnabled ->
            if (filterEnabled) {
                switchFilter.setText(R.string.filter_enabled)
                switchFilter.typeface = Typeface.DEFAULT_BOLD
                //                viewModel.startProcessing()

                val frequencies = viewModel.getFrequencies()
                val gains = viewModel.getGains()
                val sb = StringBuilder()
                frequencies.forEachIndexed { i, _ ->
                    sb.append(frequencies[i].toString() + "Hz: " + gains[i].toString() + "dB\n")
                }
                val text = sb.toString()

                AudioProcessorService.start(this, text)
            } else {
                switchFilter.setText(R.string.filter_disabled)
                switchFilter.typeface = Typeface.DEFAULT
                //                viewModel.stopProcessing()

                AudioProcessorService.stop(this)
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
