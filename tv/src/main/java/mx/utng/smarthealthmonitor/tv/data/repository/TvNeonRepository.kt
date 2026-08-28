package mx.utng.smarthealthmonitor.tv.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.tv.data.remote.LecturaFcDto
import mx.utng.smarthealthmonitor.tv.data.remote.NeonClient
import mx.utng.smarthealthmonitor.tv.data.remote.NeonRequest

/**
 * Repositorio para que la TV consulte datos globales directamente desde Neon.
 */
class TvNeonRepository {

    /**
     * Obtiene el historial completo de lecturas de todos los dispositivos.
     */
    suspend fun obtenerHistorialCompleto(limite: Int = 50): List<LecturaFcDto> = withContext(Dispatchers.IO) {
        try {
            val query = "SELECT id, bpm, estado, dispositivo, hora, created_at FROM lecturas_fc ORDER BY created_at DESC LIMIT $1"
            val response = NeonClient.apiService.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(query, listOf(limite.toString()))
            )
            response.rows
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Obtiene promedios de BPM agrupados por dispositivo.
     */
    suspend fun obtenerEstadisticas(): List<LecturaFcDto> = withContext(Dispatchers.IO) {
        try {
            val query = "SELECT dispositivo, ROUND(AVG(bpm))::int AS bpm, 'Promedio' AS estado, MAX(hora) AS hora FROM lecturas_fc GROUP BY dispositivo"
            val response = NeonClient.apiService.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(query)
            )
            response.rows
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
