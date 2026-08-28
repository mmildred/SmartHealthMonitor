package mx.utng.smarthealthmonitor.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO para gestionar las operaciones de la base de datos local de lecturas de FC.
 * Incluye métodos específicos para el motor de sincronización.
 */
@Dao
interface LecturaFCDao {

    @Query("SELECT * FROM lecturas_fc ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<LecturaFC>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lectura: LecturaFC): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(lectura: LecturaFC)

    @Query("SELECT * FROM lecturas_fc WHERE sincronizado = 0")
    suspend fun obtenerNoSincronizados(): List<LecturaFC>

    @Query("UPDATE lecturas_fc SET sincronizado = 1 WHERE id = :id")
    suspend fun marcarSincronizado(id: Long)

    @Query("SELECT COUNT(*) FROM lecturas_fc WHERE sincronizado = 0")
    fun contarPendientes(): Flow<Int>

    // Métodos heredados para mantenimiento
    @Query("SELECT COUNT(*) FROM lecturas_fc")
    suspend fun contarRegistros(): Int

    @Query("DELETE FROM lecturas_fc WHERE timestamp < :limite")
    suspend fun limpiarViejos(limite: Long)
    
    @Query("SELECT * FROM lecturas_fc ORDER BY timestamp DESC LIMIT 50")
    fun obtenerUltimas(): Flow<List<LecturaFC>>
}
