package mx.utng.smarthealthmonitor.data.sync

import android.content.Context
import androidx.work.*
import mx.utng.smarthealthmonitor.data.db.SmartHealthDB
import mx.utng.smarthealthmonitor.data.repository.SyncRepository
import java.util.concurrent.TimeUnit

/**
 * Worker encargado de la sincronización periódica de datos en segundo plano.
 * Sube lecturas pendientes y descarga el historial más reciente desde Neon.
 */
class NeonSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val database = SmartHealthDB.getDatabase(applicationContext)
            val repository = SyncRepository(database.lecturaDao())

            // 1. Intentar enviar lecturas que se quedaron offline
            repository.enviarPendientes()

            // 2. Traer las últimas lecturas de la nube para mantener el historial actualizado
            repository.sincronizarDesdeNeon(100)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            // Reintentar en la próxima oportunidad si el fallo fue temporal
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "NeonSyncWork"

        /**
         * Programa la ejecución periódica del worker cada 30 minutos.
         * Requiere conexión a internet activa.
         */
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<NeonSyncWorker>(
                30, TimeUnit.MINUTES
            ).setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
        }
    }
}
