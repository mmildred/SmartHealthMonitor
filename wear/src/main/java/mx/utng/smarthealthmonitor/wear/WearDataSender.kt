package mx.utng.smarthealthmonitor.wear

import android.content.Context
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(context: Context) {
    private val messageClient = Wearable.getMessageClient(context)
    private val nodeClient = Wearable.getNodeClient(context)

    suspend fun enviarFC(bpm: Int) {
        try {
            val nodes = nodeClient.connectedNodes.await()
            for (node in nodes) {
                messageClient.sendMessage(
                    node.id,
                    "/heart-rate",
                    bpm.toString().toByteArray()
                ).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}