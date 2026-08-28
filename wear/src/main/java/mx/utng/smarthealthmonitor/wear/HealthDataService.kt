package mx.utng.smarthealthmonitor.wear

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.await
import mx.utng.smarthealthmonitor.wear.data.WearNeonRepository

class HealthDataService : PassiveListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var wearDataSender: WearDataSender
    private val neonRepo = WearNeonRepository()

    override fun onCreate() {
        super.onCreate()
        wearDataSender = WearDataSender(this) // S6: MessageClient
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        val fcDataPoints = dataPoints.getData(DataType.HEART_RATE_BPM)
        fcDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<Double>) {
                val bpm = dataPoint.value.toInt()
                val estado = if (bpm in 60..100) "Normal" else "Irregular"
                
                scope.launch { 
                    // 1. Enviar al teléfono vía BLE
                    wearDataSender.enviarFC(bpm)
                    
                    // 2. Publicar directamente en Neon
                    neonRepo.publicarLectura(bpm, estado)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        suspend fun registrar(context: Context) {
            val hsClient = HealthServices.getClient(context)
            val passiveClient = hsClient.passiveMonitoringClient

            val config = PassiveListenerConfig.builder()
                .setDataTypes(setOf(DataType.HEART_RATE_BPM))
                .setShouldUserActivityInfoBeRequested(true)
                .build()

            passiveClient.setPassiveListenerServiceAsync(
                HealthDataService::class.java,
                config
            ).await()
        }
    }
}