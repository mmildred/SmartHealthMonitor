package mx.utng.smarthealthmonitor.wear.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.*
import androidx.wear.compose.foundation.rotary.*
import androidx.wear.compose.material.*
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.wear.presentation.WearDashboardViewModel

@Composable
fun WearHistorialScreen(
    onBack: () -> Unit,
    viewModel: WearDashboardViewModel = viewModel()
) {
    val historial by viewModel.historial.collectAsState()
    val listState = rememberScalingLazyListState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .rotaryScrollable(
                    behavior = RotaryScrollableDefaults.behavior(scrollableState = listState),
                    focusRequester = focusRequester
                )
                .focusRequester(focusRequester)
        ) {
            item {
                ListHeader {
                    Text("Historial (${historial.size})")
                }
            }

            if (historial.isEmpty()) {
                item {
                    Text(
                        text = "Sin registros",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

            items(historial, key = { it.id }) { lectura ->
                WearFilaHistorial(lectura = lectura)
            }
        }
    }
}

@Composable
fun WearFilaHistorial(lectura: LecturaFC) {
    val isNormal = lectura.esNormal
    val colorFC = if (isNormal) MaterialTheme.colors.primary else MaterialTheme.colors.error

    Chip(
        onClick = { /* Opcional: ver detalle */ },
        label = {
            Text(
                text = "${lectura.valorBpm} bpm",
                color = colorFC
            )
        },
        secondaryLabel = {
            Text(text = lectura.hora)
        },
        colors = ChipDefaults.secondaryChipColors(),
        modifier = Modifier.fillMaxWidth()
    )
}