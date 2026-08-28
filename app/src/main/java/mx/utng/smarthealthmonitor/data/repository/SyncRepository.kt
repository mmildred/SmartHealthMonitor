package mx.utng.smarthealthmonitor.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.data.remote.NeonClient
import mx.utng.smarthealthmonitor.data.remote.NeonRequest

/**
 * Repositorio que implementa la estrategia Offline-First.
 * Coordina la persistencia local en Room con la sincronización remota en Neon.
 */
class SyncRepository(private val dao: LecturaFCDao) {

    /**
     * Observa todo el historial de lecturas almacenado localmente.
     */
    fun observarHistorial(): Flow<List<LecturaFC>> = dao.obtenerTodas()

    /**
     * Inserta una lectura localmente y trata de sincronizarla inmediatamente.
     */
    suspend fun insertarLectura(lectura: LecturaFC) = withContext(Dispatchers.IO) {
        // 1. Persistencia local obligatoria
        val localId = dao.insertar(lectura.copy(sincronizado = false))
        
        // 2. Intento de sincronización inmediata
        try {
            sincronizarHaciaNeon(lectura)
            dao.marcarSincronizado(localId)
        } catch (e: Exception) {
            // Si falla (ej. sin red), se queda en Room como sincronizado = false
            e.printStackTrace()
        }
    }

    /**
     * Ejecuta una consulta SQL INSERT en el servidor Neon a través de HTTP.
     */
    private suspend fun sincronizarHaciaNeon(lectura: LecturaFC) {
        val query = "INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora) VALUES ($1, $2, $3, $4) RETURNING id"
        val params = listOf(
            lectura.valorBpm.toString(),
            lectura.estado,
            lectura.dispositivo,
            lectura.hora
        )
        
        NeonClient.apiService.executeQuery(
            auth = NeonClient.AUTH_HEADER,
            connStr = NeonClient.CONN_STRING,
            request = NeonRequest(query, params)
        )
    }

    /**
     * Descarga las últimas lecturas desde Neon y las actualiza en la base de datos local.
     */
    suspend fun sincronizarDesdeNeon(limite: Int = 50) = withContext(Dispatchers.IO) {
        try {
            val query = "SELECT id, bpm, estado, dispositivo, hora FROM lecturas_fc ORDER BY created_at DESC LIMIT $1"
            val response = NeonClient.apiService.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(query, listOf(limite.toString()))
            )
            
            response.rows.forEach { dto ->
                dao.upsert(
                    LecturaFC(
                        valorBpm = dto.bpm,
                        estado = dto.estado,
                        dispositivo = dto.dispositivo,
                        hora = dto.hora,
                        sincronizado = true
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Busca registros locales no sincronizados y trata de enviarlos al servidor.
     */
    suspend fun enviarPendientes() = withContext(Dispatchers.IO) {
        val pendientes = dao.obtenerNoSincronizados()
        pendientes.forEach { lectura ->
            try {
                sincronizarHaciaNeon(lectura)
                dao.marcarSincronizado(lectura.id.toLong())
            } catch (e: Exception) {
                // Si uno falla, detenemos el proceso para reintentar más tarde
                return@withContext
            }
        }
    }
}
