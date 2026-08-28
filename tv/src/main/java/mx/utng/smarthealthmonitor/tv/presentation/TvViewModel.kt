package mx.utng.smarthealthmonitor.tv.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState

class TvViewModel(private val repository: SmartHealthRepository) : ViewModel() {

    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    init {
        // Recolectar historial de lecturas
        viewModelScope.launch {
            repository.obtenerHistorial()
                .catch { e ->
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { lecturas ->
                    _state.update { it.copy(lecturas = lecturas, isLoading = false) }
                }
        }

        // Recolectar frecuencia cardíaca actual
        viewModelScope.launch {
            repository.fcFlow.collect { fc ->
                _state.update { it.copy(fcActual = fc) }
            }
        }
    }
}