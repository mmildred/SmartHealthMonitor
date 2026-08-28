package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvDetailScreen(
    lecturaId: Int,
    viewModel: TvViewModel,
    navController: NavController
) {
    // En un caso real, buscaríamos la lectura específica en el ViewModel
    val lectura = remember(lecturaId) {
        viewModel.state.value.lecturas.find { it.id == lecturaId }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B4A))
            .padding(48.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }

            if (lectura != null) {
                Text(
                    text = "Detalle de Lectura #${lectura.id}",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    Column {
                        Text("Frecuencia:", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                        Text("${lectura.bpm} bpm", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                    }
                    Column {
                        Text("Estado:", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                        Text(lectura.estado, style = MaterialTheme.typography.headlineLarge, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate("playback") },
                    modifier = Modifier.width(300.dp)
                ) {
                    Text("Ver Video de Salud")
                }
            } else {
                Text("Lectura no encontrada", color = Color.White)
            }
        }
    }
}
