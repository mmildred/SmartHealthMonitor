package mx.utng.smarthealthmonitor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mx.utng.smarthealthmonitor.ui.screens.HistorialScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            // Aquí iría el LoginScreen que ya existe en el proyecto
        }
        composable(Screen.Dashboard.route) {
            // Aquí iría el DashboardScreen
        }
        composable(Screen.Historial.route) {
            HistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}