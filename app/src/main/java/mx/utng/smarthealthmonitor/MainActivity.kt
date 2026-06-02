package mx.utng.smarthealthmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import mx.utng.smarthealthmonitor.ui.screens.DashboardScreen
import mx.utng.smarthealthmonitor.ui.screens.LoginScreen
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHealthMonitorTheme {
                SmartHealthApp()
            }
        }
    }
}

@Composable
fun SmartHealthApp() {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        DashboardScreen(
            onHistoricalClick = { /* Navegar a historial */ },
            onAlertClick = { /* Mostrar alertas */ }
        )
    } else {
        LoginScreen(
            onLoginSuccess = {
                isLoggedIn = true
            }
        )
    }
}