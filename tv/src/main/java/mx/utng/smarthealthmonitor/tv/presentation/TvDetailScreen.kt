package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvDetailScreen(
    lecturaId: Int,
    navController: NavController,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lectura = state.lecturas.find { it.id == lecturaId } ?: return

    val firstBtnFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        firstBtnFocus.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B4A))
            .padding(64.dp),
        horizontalArrangement = Arrangement.spacedBy(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Panel Izquierdo (weight 0.4f)
        Column(
            modifier = Modifier.weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(Color(0xFF1565C0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "❤",
                    fontSize = 80.sp,
                    color = Color.White
                )
            }

            Text(
                text = "${lectura.bpm} bpm",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = lectura.estado,
                style = MaterialTheme.typography.headlineSmall,
                color = if (lectura.estado == "Normal") Color.Green else Color.Red
            )

            Text(
                text = "ID: ${lectura.id}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Panel Derecho (weight 0.6f)
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Recomendaciones de Salud",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Tu ritmo cardíaco actual es ${lectura.estado.lowercase()}. Te sugerimos ver el siguiente video para mantener un estilo de vida saludable.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Reproducir
            Surface(
                onClick = { navController.navigate("playback") },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp)
                    .focusRequester(firstBtnFocus),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = Color(0xFF1B5E20),
                    focusedContainerColor = Color(0xFF76FF03),
                    pressedContainerColor = Color(0xFF00C853)
                ),
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp))
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "▶  Reproducir",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Botón Volver
            Surface(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = Color(0xFF37474F),
                    focusedContainerColor = Color(0xFF90A4AE),
                    pressedContainerColor = Color(0xFF263238)
                ),
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp))
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "← Volver",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
