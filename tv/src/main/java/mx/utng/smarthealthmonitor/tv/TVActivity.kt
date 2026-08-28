package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.presentation.TvCatalogScreen
import mx.utng.smarthealthmonitor.tv.presentation.TvViewModel
import mx.utng.smarthealthmonitor.tv.presentation.TvViewModelFactory

class TVActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar repositorio
        SmartHealthRepository.init(this)

        val factory = TvViewModelFactory(SmartHealthRepository)
        val viewModel = ViewModelProvider(this, factory)[TvViewModel::class.java]

        setContent {
            MaterialTheme {
                TvCatalogScreen(viewModel = viewModel)
            }
        }
    }
}
