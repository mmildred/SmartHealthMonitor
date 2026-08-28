package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import mx.utng.smarthealthmonitor.tv.presentation.TvCatalogScreen
import mx.utng.smarthealthmonitor.tv.presentation.TvDetailScreen
import mx.utng.smarthealthmonitor.tv.presentation.TvPlaybackScreen
import mx.utng.smarthealthmonitor.tv.presentation.TvViewModel

/**
 * Actividad principal para Android TV con Navegación.
 */
class TVActivity : ComponentActivity() {
    
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val viewModel: TvViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "catalog"
                ) {
                    composable("catalog") {
                        TvCatalogScreen(
                            viewModel = viewModel,
                            onCardClick = { id ->
                                navController.navigate("detail/$id")
                            }
                        )
                    }

                    composable(
                        route = "detail/{lecturaId}",
                        arguments = listOf(navArgument("lecturaId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("lecturaId") ?: 0
                        TvDetailScreen(
                            lecturaId = id,
                            viewModel = viewModel,
                            navController = navController
                        )
                    }

                    composable("playback") {
                        TvPlaybackScreen(navController = navController)
                    }
                }
            }
        }
    }
}
