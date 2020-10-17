package soy.gabimoreno.audioclean.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import soy.gabimoreno.audioclean.data.preferences.EqualizationDatasource
import soy.gabimoreno.audioclean.domain.Equalization
import soy.gabimoreno.audioclean.domain.usecase.GetEqualizationUseCase
import soy.gabimoreno.audioclean.framework.AudioProcessor
import soy.gabimoreno.audioclean.framework.KLog
import soy.gabimoreno.audioclean.presentation.customview.fader.Fader

class MainViewModel(
    private val audioProcessor: AudioProcessor,
    private val equalizationDatasource: EqualizationDatasource,
    private val getEqualizationUseCase: GetEqualizationUseCase
) : ViewModel() {

    private var _info = MutableLiveData<String>()
    val info: LiveData<String> = _info

    private var _equalizations = MutableLiveData<List<Equalization>>()
    val equalizations: LiveData<List<Equalization>> = _equalizations

    private var _currentEqualizationPosition = MutableLiveData<Int>()
    val currentEqualizationPosition = _currentEqualizationPosition

    init {
        audioProcessor.setListener {
            _info.value = it.toString()
        }
        audioProcessor.init()

        _equalizations.value = mutableListOf()
        _currentEqualizationPosition.value = 0 // TODO: 0 -> Current position
        loadAllEqualizations()
    }

    private val faders = mutableListOf<Fader>()

    fun startProcessing() {
        audioProcessor.start()
    }

    fun stopProcessing() {
        audioProcessor.stop()
    }

    fun getFrequencies(): IntArray = audioProcessor.getFrequencies()
    fun getGains(): IntArray = audioProcessor.getGains()

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
            }, { equalizations ->
                _equalizations.value = equalizations
            })
    }

    fun loadEqualization(position: Int) {
        equalizationDatasource.load(EqualizationDatasource.Positions.values()[position])
            .fold({
                KLog.e("Error loading equalization")
            }, { equalization ->
                faders.forEachIndexed { i, fader ->
                    fader.setGain(equalization.frequencyGains[i].gain)
                }
            })
    }

    fun saveEqualization(equalizationName: String) {
        val frequencies = faders.map { it.frequency }.toIntArray()
        val gains = faders.map { it.getGain() }.toIntArray()
        val equalization = getEqualizationUseCase(equalizationName, frequencies, gains)
        equalizationDatasource.save(equalization)

        val list = _equalizations.value as MutableList<Equalization>
        list.add(equalization)
        _equalizations.value = list
    }

    fun onDeleteAllClicked() {
        equalizationDatasource.deleteAll()
        _equalizations.value = listOf()
    }

    fun resetFaders() {
        faders.forEach {
            it.setGain(0)
        }
    }
}
