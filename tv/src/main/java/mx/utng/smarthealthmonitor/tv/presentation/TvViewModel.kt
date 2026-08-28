package mx.utng.smarthealthmonitor.tv.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.domain.model.LecturaFC
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState

class TvViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TvUiState(
        isLoading = true,
        fcActual = 75,
        lecturas = getMockData()
    ))
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    private val repository = SmartHealthRepository

    init {
        repository.init(application)
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            repository.obtenerHistorial()
                .catch { e ->
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { lecturas ->
                    val mapped = if (lecturas.isEmpty()) {
                        getMockData()
                    } else {
                        lecturas.map { dbLectura ->
                            LecturaFC(
                                id = dbLectura.id,
                                bpm = dbLectura.valorBpm,
                                timestamp = dbLectura.timestamp,
                                estado = if (dbLectura.esNormal) "Normal" else "Irregular"
                            )
                        }
                    }
                    _state.update { it.copy(lecturas = mapped, isLoading = false) }
                }
        }

        viewModelScope.launch {
            repository.fcFlow.collect { fc ->
                if (fc > 0) {
                    _state.update { it.copy(fcActual = fc) }
                }
            }
        }
    }

    private fun getMockData(): List<LecturaFC> {
        val now = System.currentTimeMillis()
        return listOf(
            LecturaFC(1, 72, now - 3600000, "Normal"),
            LecturaFC(2, 85, now - 2700000, "Normal"),
            LecturaFC(3, 110, now - 1800000, "Alerta"),
            LecturaFC(4, 68, now - 900000, "Normal"),
            LecturaFC(5, 75, now, "Normal")
        )
    }
}
