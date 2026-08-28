package mx.utng.smarthealthmonitor.wear.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.wear.data.remote.LecturaFcDto
import mx.utng.smarthealthmonitor.wear.data.remote.NeonClient
import mx.utng.smarthealthmonitor.wear.data.remote.NeonRequest
import java.text.SimpleDateFormat
import java.util.*

class WearNeonRepository {

    suspend fun publicarLectura(bpm: Int, estado: String) = withContext(Dispatchers.IO) {
        try {
            val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val query = "INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora) VALUES ($1, $2, $3, $4)"
            val params = listOf(bpm.toString(), estado, "wear", hora)

            NeonClient.apiService.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(query, params)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun obtenerUltimasLecturas(): List<LecturaFcDto> = withContext(Dispatchers.IO) {
        try {
            val query = "SELECT id, bpm, estado, dispositivo, hora FROM lecturas_fc WHERE dispositivo = 'wear' ORDER BY created_at DESC LIMIT 5"
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
