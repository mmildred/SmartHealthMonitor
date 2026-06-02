package mx.utng.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.utng.smarthealthmonitor.ui.viewmodel.DashboardViewModel
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.smarthealthmonitor.data.SmartHealthRepository

// Imports para los iconos
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistoricalClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val historial = viewModel.historical

    SmartHealthMonitorTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("SmartHealth Monitor") },
                    actions = {
                        IconButton(onClick = onAlertClick) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Alertas"
                            )
                        }
                        IconButton(onClick = onHistoricalClick) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "Historial"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Frecuencia Cardíaca",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "$fc BPM",
                                style = MaterialTheme.typography.displayMedium
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Pasos hoy",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "$pasos",
                                style = MaterialTheme.typography.displayMedium
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Historial de FC (últimos 7 días)",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                items(historial) { valor ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Lectura")
                            Text(text = "$valor BPM")
                        }
                    }
                }

                // Botón de simulación para pruebas (SIN BuildConfig para evitar error)
                item {
                    Button(
                        onClick = {
                            val fcSimulado = (60..110).random()
                            val pasosSimulado = (3000..8000).random()
                            SmartHealthRepository.actualizarFC(fcSimulado)
                            SmartHealthRepository.actualizarPasos(pasosSimulado)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Simular dato del wearable")
                    }
                }
            }
        }
    }
}