package mx.utng.smarthealthmonitor.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

/**
 * Pantalla de Alerta de Emergencia para notificar contactos cuando el FC es crítico.
 * Implementado siguiendo lineamientos de Material Design 3.
 *
 * @param fc El valor de frecuencia cardíaca actual.
 * @param onDismiss Acción al cancelar o cerrar el diálogo.
 * @param onConfirmar Acción al confirmar el envío de la alerta.
 */
@Composable
fun AlertaScreen(
    fc: Int,
    onDismiss: () -> Unit,
    onConfirmar: () -> Unit
) {
    var enviando by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Enviar alerta de emergencia",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "FC actual: $fc bpm",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Se notificará a tus contactos de emergencia.\nEsta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    enviando = true
                    onConfirmar()
                },
                enabled = !enviando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                if (enviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onError,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "CONFIRMAR ALERTA",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        }
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun AlertaScreenPreview() {
    SmartHealthMonitorTheme {
        AlertaScreen(
            fc = 145,
            onDismiss = {},
            onConfirmar = {}
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AlertaScreenDarkPreview() {
    SmartHealthMonitorTheme {
        AlertaScreen(
            fc = 145,
            onDismiss = {},
            onConfirmar = {}
        )
    }
}
