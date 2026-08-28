package mx.utng.smarthealthmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.data.MockData
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.db.LecturaFC

/**
 * ViewModel del Dashboard con manejo seguro de datos y estados.
 * Utiliza StateFlow para proveer actualizaciones reactivas a la UI.
 */
class DashboardViewModel : ViewModel() {

    // Frecuencia cardíaca actual protegida con valores de prueba (MockData)
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) MockData.fcActual else it }
        .catch { emit(MockData.fcActual) } // Evitar crash si el Flow falla
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.fcActual
        )

    // Historial de lecturas desde Room. 
    // Se utiliza catch para evitar que un error en la DB cierre la app.
    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .catch { emit(emptyList()) } 
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}
