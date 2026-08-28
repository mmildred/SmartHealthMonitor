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
import mx.utng.smarthealthmonitor.tv.data.repository.TvNeonRepository
import mx.utng.smarthealthmonitor.tv.domain.model.LecturaFC
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState

/**
 * Categorías de filtrado para la frecuencia cardíaca en TV.
 */
enum class FiltroFc { TODOS, NORMAL, ALERTA }

/**
 * ViewModel optimizado para TV con consulta directa a Neon y estadísticas.
 */
class TvViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TvUiState(isLoading = true))
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    // Estado del filtro seleccionado
    var filtroSeleccionado by mutableStateOf(FiltroFc.TODOS)
        private set

    private val localRepository = SmartHealthRepository
    private val neonRepository = TvNeonRepository()

    // Estadísticas agrupadas por dispositivo
    private val _estadisticas = MutableStateFlow<List<LecturaFC>>(emptyList())
    val estadisticas: StateFlow<List<LecturaFC>> = _estadisticas.asStateFlow()

    init {
        localRepository.init(application)
        refresh()
    }

    /**
     * Refresca los datos consultando tanto Room local como Neon remoto.
     */
    fun refresh() {
        _state.update { it.copy(isLoading = true) }
        cargarDatosLocales()
        cargarDatosRemotos()
    }

    private fun cargarDatosLocales() {
        viewModelScope.launch {
            localRepository.fcFlow.collect { fc ->
                if (fc > 0) {
                    _state.update { it.copy(fcActual = fc) }
                }
            }
        }
    }

    private fun cargarDatosRemotos() {
        viewModelScope.launch {
            // 1. Obtener historial completo de la nube
            val remoto = neonRepository.obtenerHistorialCompleto(100)
            if (remoto.isNotEmpty()) {
                val mapped = remoto.map { dto ->
                    LecturaFC(
                        id = dto.id,
                        bpm = dto.bpm,
                        timestamp = System.currentTimeMillis(), // O parsear created_at
                        estado = dto.estado
                    )
                }
                _state.update { it.copy(lecturas = mapped, isLoading = false) }
            } else {
                // Fallback a Mock si no hay red o está vacío
                _state.update { it.copy(lecturas = getMockData(), isLoading = false) }
            }

            // 2. Obtener estadísticas (promedios)
            val stats = neonRepository.obtenerEstadisticas()
            _estadisticas.value = stats.map { dto ->
                LecturaFC(
                    id = 0,
                    bpm = dto.bpm,
                    timestamp = 0,
                    estado = "${dto.dispositivo}: ${dto.estado}"
                )
            }
        }
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

    private fun getMockData(): List<LecturaFC> {
        val now = System.currentTimeMillis()
        val hourMillis = 3600000L
        return listOf(
            LecturaFC(1, 72, now - (hourMillis * 12), "Normal"),
            LecturaFC(2, 68, now - (hourMillis * 11), "Normal"),
            LecturaFC(3, 115, now - (hourMillis * 10), "Alerta"),
            LecturaFC(4, 78, now - (hourMillis * 9), "Normal"),
            LecturaFC(5, 125, now - (hourMillis * 8), "Alerta")
        )
    }
}
