package soy.gabimoreno.audioclean.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import soy.gabimoreno.audioclean.data.analytics.AnalyticsTrackerComponent
import soy.gabimoreno.audioclean.data.analytics.error.ErrorTrackerComponent
import soy.gabimoreno.audioclean.data.preferences.EqualizationDatasource
import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.domain.usecase.GetEqualizationUseCase
import soy.gabimoreno.audioclean.framework.AudioProcessor
import soy.gabimoreno.audioclean.framework.KLog
import soy.gabimoreno.audioclean.presentation.analytics.MainEvents
import soy.gabimoreno.audioclean.presentation.customview.fader.Fader

class MainViewModel(
    private val audioProcessor: AudioProcessor,
    private val equalizationDatasource: EqualizationDatasource,
    private val getEqualizationUseCase: GetEqualizationUseCase,
    private val analyticsTrackerComponent: AnalyticsTrackerComponent,
    private val errorTrackerComponent: ErrorTrackerComponent
) : ViewModel() {

    init {
        analyticsTrackerComponent.trackEvent(MainEvents.ScreenMain)
    }

    private var _info = MutableLiveData<String>()
    val info: LiveData<String> = _info

    private var _equalizations = MutableLiveData<List<Equalization>>()
    val equalizations: LiveData<List<Equalization>> = _equalizations

    private var _currentEqualizationPosition = MutableLiveData<Int>()
    val currentEqualizationPosition = _currentEqualizationPosition

    init {
        audioProcessor.setListener { audioSessionId ->
            _info.value = audioSessionId.toString()
            analyticsTrackerComponent.trackEvent(MainEvents.DataAudioSession(audioSessionId))
        }
        audioProcessor.init()

        _equalizations.value = mutableListOf()
        _currentEqualizationPosition.value = 0
        loadAllEqualizations()
    }

    // TODO: After using the base view model
//    fun handleShareClicked() {
//        viewModelScope.launch {
//            analyticsTrackerComponent.trackEvent(MainEvents.ClickShare)
//            sendViewEvent(ViewEvents.Share)
//        }
//    }
//
//    fun handleEmailClicked() {
//        viewModelScope.launch {
//            analyticsTrackerComponent.trackEvent(MainEvents.ClickEmail)
//            sendViewEvent(ViewEvents.SendEmail)
//        }
//    }
//
//    fun handleRateClicked() {
//        viewModelScope.launch {
//            analyticsTrackerComponent.trackEvent(MainEvents.ClickRate)
//            sendViewEvent(ViewEvents.Rate)
//        }
//    }
//
//    fun handleInfoClicked() {
//        viewModelScope.launch {
//            analyticsTrackerComponent.trackEvent(MainEvents.ClickInfo)
//            sendViewEvent(ViewEvents.NavigateToWeb("http://appacoustic.com"))
//        }
//    }

    private val faders = mutableListOf<Fader>()

    fun startProcessing() {
        audioProcessor.start()
    }

    fun stopProcessing() {
        audioProcessor.stop()
    }

    fun releaseProcessing() {
        audioProcessor.release()
    }

    fun getFrequencies(): IntArray = audioProcessor.getFrequencies()
    fun getGains(): IntArray = audioProcessor.getGains()

    fun getNumberOfEqualizations(): Int = equalizations.value?.size ?: 0

    fun setVolume(i: Int, gain: Int) {
        audioProcessor.setVolume(i, gain)
    }

    fun addFader(fader: Fader) {
        faders.add(fader)
        fader.setListener(object : Fader.Listener {
            override fun onGainChanged(i: Int, gain: Int) {
                setVolume(i, gain)
            }
        })
    }

    private fun loadAllEqualizations() {
        equalizationDatasource.loadAll()
            .fold({
                KLog.e("Error when trying to load all the equalizations")
                errorTrackerComponent.trackError(it)
            }, { equalizations ->
                _equalizations.value = equalizations
            })
    }

    fun loadEqualization(position: Int) {
        equalizationDatasource.load(EqualizationDatasource.Positions.values()[position])
            .fold({
                KLog.e("Error loading equalization")
                errorTrackerComponent.trackError(it)
            }, { equalization ->
                faders.forEachIndexed { i, fader ->
                    fader.setGain(equalization.frequencyGains[i].gain)
                }
            })
    }

    fun onSaveEqualizationClicked(equalizationName: String) {
        val frequencies = faders.map { it.frequency }.toIntArray()
        val gains = faders.map { it.getGain() }.toIntArray()
        val equalization = getEqualizationUseCase(equalizationName, frequencies, gains)
        equalizationDatasource.save(equalization)
        analyticsTrackerComponent.trackEvent(MainEvents.ClickSaveEqualization(equalization))

        val list = _equalizations.value as MutableList<Equalization>
        list.add(equalization)
        _currentEqualizationPosition.value = list.size - 1
        _equalizations.value = list
    }

    fun onBtnPreset1clicked() {
        resetFaders()
        faders[5].setGain(2)
        faders[6].setGain(4)
        faders[7].setGain(2)
    }

    fun onBtnPreset2clicked() {
        resetFaders()
        faders[0].setGain(-6)
        faders[1].setGain(-6)
    }

    fun onBtnPreset3clicked() {
        faders.forEach {
            it.setGain(7)
        }
    }

    fun onDeleteAllClicked() {
        equalizationDatasource.deleteAll()
        _equalizations.value = mutableListOf()
    }

    fun resetFaders() {
        faders.forEach {
            it.setGain(0)
        }
    }
}
