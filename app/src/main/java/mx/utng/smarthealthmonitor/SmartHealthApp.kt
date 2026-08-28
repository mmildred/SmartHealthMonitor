package mx.utng.smarthealthmonitor

import android.app.Application
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.sync.NeonSyncWorker

/**
 * Clase de aplicación principal.
 * Inicializa el repositorio único y programa las tareas en segundo plano.
 */
class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Inicialización de Room y Repositorio base
        SmartHealthRepository.init(this)
        
        // Programación de la sincronización periódica con PostgreSQL Neon
        NeonSyncWorker.schedule(this)
    }
}
