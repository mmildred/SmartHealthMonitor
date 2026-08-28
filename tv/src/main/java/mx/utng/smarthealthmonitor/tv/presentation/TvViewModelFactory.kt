package mx.utng.smarthealthmonitor.tv.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mx.utng.smarthealthmonitor.data.SmartHealthRepository

class TvViewModelFactory(private val repository: SmartHealthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TvViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}