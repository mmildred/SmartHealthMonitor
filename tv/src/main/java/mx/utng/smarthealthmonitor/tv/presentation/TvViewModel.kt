package mx.utng.smarthealthmonitor.tv.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.domain.model.LecturaFC
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState

/**
 * ViewModel optimizado para TV con inicialización segura y datos de prueba (Mock Data).
 */
class TvViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TvUiState(
        isLoading = true,
        fcActual = 75, // Valor por defecto para asegurar visualización en el arranque
        lecturas = getMockData() // Datos de prueba iniciales
    ))
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    private val repository = SmartHealthRepository

    init {
        // Aseguramos que el repositorio esté inicializado para el proceso de la TV
        repository.init(application)
        cargarDatos()
    }

    private fun cargarDatos() {
        // Recolectar historial de lecturas
        viewModelScope.launch {
            repository.obtenerHistorial()
                .catch { e ->
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { lecturas ->
                    val mapped = if (lecturas.isEmpty()) {
                        // Si Room está vacío, mantenemos los datos de prueba
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

        // Recolectar frecuencia cardíaca actual
        viewModelScope.launch {
            repository.fcFlow.collect { fc ->
                // Solo actualizamos si el valor es mayor a 0 para no sobreescribir el mock con ceros
                if (fc > 0) {
                    _state.update { it.copy(fcActual = fc) }
                }
            }
        }
    }

    /**
     * Genera datos de prueba (Mock Data) para asegurar que la UI de TV renderice tarjetas
     * incluso si la base de datos local está vacía.
     */
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
