package mx.utng.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToHistorial: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (mostrarAlerta) {
        AlertaScreen(
            fc = fc,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = {
                mostrarAlerta = false
                scope.launch {
                    snackbarHost.showSnackbar(
                        message = "✅ Alerta enviada a tus contactos de emergencia",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Dashboard") })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHost) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarAlerta = true },
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ) {
                Icon(Icons.Default.Warning, contentDescription = "Alerta")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Frecuencia Cardíaca Actual", style = MaterialTheme.typography.titleMedium)
            Text("$fc BPM", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.primary)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(onClick = onNavigateToHistorial) {
                Text("Ver Historial")
            }
        }
    }
}