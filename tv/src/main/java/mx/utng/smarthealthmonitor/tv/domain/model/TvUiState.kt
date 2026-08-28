package mx.utng.smarthealthmonitor.tv.domain.model

data class LecturaFC(
    val id: Int,
    val bpm: Int,
    val timestamp: Long,
    val estado: String
)

data class TvUiState(
    val fcActual: Int = 0,
    val lecturas: List<LecturaFC> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
