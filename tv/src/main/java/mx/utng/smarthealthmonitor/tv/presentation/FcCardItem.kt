package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import mx.utng.smarthealthmonitor.tv.domain.model.LecturaFC

/**
 * Componente de tarjeta individual optimizado para navegación con D-pad en Android TV.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FcCardItem(
    lectura: LecturaFC,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .width(200.dp)
            .height(120.dp),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color(0xFF1565C0),
            focusedContainerColor = Color(0xFF42A5F5),
            pressedContainerColor = Color(0xFF0D47A1)
        ),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${lectura.bpm} bpm",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Column {
                Text(
                    text = lectura.estado,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "ID: ${lectura.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}
