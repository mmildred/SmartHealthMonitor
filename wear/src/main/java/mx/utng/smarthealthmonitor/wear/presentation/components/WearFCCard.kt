package mx.utng.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*

@Composable
fun WearFCCard(fc: Int, modifier: Modifier = Modifier) {
    val isNormal = fc in 60..100
    val colorFC = if (isNormal) MaterialTheme.colors.primary else MaterialTheme.colors.error

    Card(
        onClick = { /* No-op */ },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "❤",
                fontSize = 20.sp,
                color = colorFC
            )
            Text(
                text = "$fc",
                style = MaterialTheme.typography.display3,
                color = colorFC
            )
            Text(
                text = "bpm",
                style = MaterialTheme.typography.caption3,
                color = MaterialTheme.colors.onSurfaceVariant
            )
        }
    }
}