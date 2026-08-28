package mx.utng.smarthealthmonitor.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Entidad de Room que representa una lectura de frecuencia cardíaca.
 * Incluye soporte para sincronización offline-first con el servidor remoto.
 */
@Entity(tableName = "lecturas_fc")
data class LecturaFC(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "bpm")
    val valorBpm: Int,
    
    @ColumnInfo(name = "estado")
    val estado: String = if (valorBpm in 60..100) "Normal" else "Irregular",
    
    @ColumnInfo(name = "dispositivo")
    val dispositivo: String = "app",
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "hora")
    val hora: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
    
    @ColumnInfo(name = "sincronizado")
    val sincronizado: Boolean = false
)
