package mx.utng.smarthealthmonitor.wear.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

@Serializable
data class NeonRequest(
    val query: String,
    val params: List<String> = emptyList()
)

@Serializable
data class NeonResponse<T>(
    val rows: List<T> = emptyList(),
    val rowCount: Int = 0
)

@Serializable
data class LecturaFcDto(
    val id: Int = 0,
    val bpm: Int,
    val estado: String,
    val dispositivo: String,
    val hora: String
)

interface NeonApiService {
    @POST("sql")
    suspend fun executeQuery(
        @Header("Authorization") auth: String,
        @Header("Neon-Connection-String") connStr: String,
        @Body request: NeonRequest
    ): NeonResponse<LecturaFcDto>
}
