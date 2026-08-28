package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import mx.utng.smarthealthmonitor.tv.presentation.TvCatalogScreen
import mx.utng.smarthealthmonitor.tv.presentation.TvViewModel

/**
 * Actividad principal para Android TV.
 */
class TVActivity : ComponentActivity() {
    
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            // El tema de TV Material 3 es esencial para componentes de androidx.tv.material3
            MaterialTheme {
                val viewModel: TvViewModel = viewModel()
                TvCatalogScreen(viewModel = viewModel)
            }
        }
    }
}
