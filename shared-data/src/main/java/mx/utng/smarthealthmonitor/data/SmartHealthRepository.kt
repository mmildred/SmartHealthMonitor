package mx.utng.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.data.db.SmartHealthDB

/**
 * Repositorio único (Singleton) para gestionar el flujo de datos de salud.
 * Centraliza el acceso a Room y a los flujos en tiempo real.
 */
object SmartHealthRepository {
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private var dao: LecturaFCDao? = null

    /**
     * Inicializa el acceso a la base de datos. Debe llamarse en el onCreate de la Application.
     */
    fun init(context: Context) {
        if (dao == null) {
            dao = SmartHealthDB.getDatabase(context).lecturaDao()
        }
    }

    /**
     * Actualiza el valor de FC y lo persiste en la base de datos de forma asíncrona.
     */
    suspend fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        // Persistir en Room de forma segura
        try {
            dao?.insertar(LecturaFC(valorBpm = bpm))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Obtiene el flujo del historial de lecturas. 
     * Retorna un flujo vacío si el DAO no ha sido inicializado.
     */
    fun obtenerHistorial(): Flow<List<LecturaFC>> = flow {
        val currentDao = dao
        if (currentDao != null) {
            emitAll(currentDao.obtenerUltimas())
        } else {
            emit(emptyList())
        }
    }
}
