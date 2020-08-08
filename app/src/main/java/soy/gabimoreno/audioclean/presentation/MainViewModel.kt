package soy.gabimoreno.audioclean.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import soy.gabimoreno.audioclean.data.preferences.EqualizationDatasource
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

    private var _equalization = MutableLiveData<String>()
    val equalization: LiveData<String> = _equalization

    init {
        audioProcessor.setListener {
            _info.value = it.toString()
        }
        audioProcessor.init()
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

    fun loadEqualization() {
        equalizationDatasource.load()
            .fold({
                KLog.e("Error loading equalization")
            }, { equalization ->
                faders.forEachIndexed { i, fader ->
                    fader.setGain(equalization.frequencyGains[i].gain)
                }
            })
    }

    fun saveEqualization() {
        val frequencies = faders.map { it.frequency }.toIntArray()
        val gains = faders.map { it.getGain() }.toIntArray()
        val equalization = getEqualizationUseCase(frequencies, gains)
        equalizationDatasource.save(equalization)
        _equalization.value = equalization.toString()
    }

    fun resetFaders() {
        faders.forEach {
            it.setGain(0)
        }
    }
}
