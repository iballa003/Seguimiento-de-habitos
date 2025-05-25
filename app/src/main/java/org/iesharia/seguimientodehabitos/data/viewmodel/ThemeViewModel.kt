package org.iesharia.seguimientodehabitos.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.iesharia.seguimientodehabitos.data.session.ThemePreferenceManager

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    val isDarkMode = ThemePreferenceManager.getDarkModeFlow(context)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            ThemePreferenceManager.setDarkMode(context, enabled)
        }
    }
}


