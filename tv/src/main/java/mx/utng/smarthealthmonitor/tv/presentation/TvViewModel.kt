package mx.utng.smarthealthmonitor.tv.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.domain.model.LecturaFC
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState

/**
 * Categorías de filtrado para la frecuencia cardíaca en TV.
 */
enum class FiltroFc { TODOS, NORMAL, ALERTA }

/**
 * ViewModel optimizado para TV con sistema de filtrado dinámico y datos ampliados.
 */
class TvViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TvUiState(
        isLoading = true,
        fcActual = 76,
        lecturas = getMockData()
    ))
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    // Estado del filtro seleccionado
    var filtroSeleccionado by mutableStateOf(FiltroFc.TODOS)
        private set

    private val repository = SmartHealthRepository

    init {
        repository.init(application)
        cargarDatos()
    }

    /**
     * Retorna las lecturas filtradas según la categoría seleccionada.
     */
    val lecturasFiltradas: List<LecturaFC>
        get() = when (filtroSeleccionado) {
            FiltroFc.TODOS -> _state.value.lecturas
            FiltroFc.NORMAL -> _state.value.lecturas.filter { it.bpm <= 100 }
            FiltroFc.ALERTA -> _state.value.lecturas.filter { it.bpm > 100 }
        }

    fun cambiarFiltro(nuevoFiltro: FiltroFc) {
        filtroSeleccionado = nuevoFiltro
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

    /**
     * Genera datos de prueba ampliados (12 registros) para facilitar la prueba
     * de desplazamiento (scrolling) en la interfaz de TV.
     */
    private fun getMockData(): List<LecturaFC> {
        val now = System.currentTimeMillis()
        val hourMillis = 3600000L
        return listOf(
            LecturaFC(1, 72, now - (hourMillis * 12), "Normal"),
            LecturaFC(2, 68, now - (hourMillis * 11), "Normal"),
            LecturaFC(3, 115, now - (hourMillis * 10), "Alerta"),
            LecturaFC(4, 78, now - (hourMillis * 9), "Normal"),
            LecturaFC(5, 125, now - (hourMillis * 8), "Alerta"),
            LecturaFC(6, 65, now - (hourMillis * 7), "Normal"),
            LecturaFC(7, 82, now - (hourMillis * 6), "Normal"),
            LecturaFC(8, 110, now - (hourMillis * 5), "Alerta"),
            LecturaFC(9, 74, now - (hourMillis * 4), "Normal"),
            LecturaFC(10, 130, now - (hourMillis * 3), "Alerta"),
            LecturaFC(11, 69, now - (hourMillis * 2), "Normal"),
            LecturaFC(12, 76, now - hourMillis, "Normal")
        )
    }
}
