package mx.utng.smarthealthmonitor.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Modelos de datos para la API HTTP de Neon.
 */
@Serializable
data class NeonRequest(
    val query: String,
    val params: List<String> = emptyList()
)

@Serializable
data class NeonResponse<T>(
    val rows: List<T> = emptyList(),
    val rowCount: Int = 0,
    val command: String = ""
)

@Serializable
data class LecturaFcDto(
    val id: Int = 0,
    val bpm: Int,
    val estado: String,
    val dispositivo: String,
    val hora: String,
    val fecha: String = "",
    val created_at: String = ""
)

/**
 * Interfaz de Retrofit para consumir la API SQL de Neon a través de HTTP.
 */
interface NeonApiService {
    @POST("sql")
    suspend fun executeQuery(
        @Header("Authorization") auth: String,
        @Header("Neon-Connection-String") connStr: String,
        @Body request: NeonRequest
    ): NeonResponse<LecturaFcDto>
}
