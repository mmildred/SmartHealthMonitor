package mx.utng.smarthealthmonitor.tv.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            val application = context.applicationContext as Application
            @Suppress("UNCHECKED_CAST")
            return TvViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
