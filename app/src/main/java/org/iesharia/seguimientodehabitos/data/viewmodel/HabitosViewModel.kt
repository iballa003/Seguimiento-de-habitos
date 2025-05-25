package org.iesharia.seguimientodehabitos.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import org.iesharia.seguimientodehabitos.data.api.HabitoService
import org.iesharia.seguimientodehabitos.data.model.Habito
import org.iesharia.seguimientodehabitos.data.session.UserSessionManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow

class HabitosViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val sessionManager = UserSessionManager(context)

    var habitos = MutableStateFlow<List<Habito>>(emptyList())
        private set

    var loading = MutableStateFlow(true)
        private set

    init {
        viewModelScope.launch {
            cargarHabitos()
        }
    }
    fun cargarHabitos() {
        viewModelScope.launch {
            val userId = sessionManager.userIdFlow.firstOrNull() ?: return@launch
            habitos.value = HabitoService.getHabitos(userId)
            loading.value = false
        }
    }

    fun recargarHabitos() {
        viewModelScope.launch {
            val userId = sessionManager.userIdFlow.firstOrNull() ?: return@launch
            val actualizados = HabitoService.getHabitos(userId)
            habitos.value = actualizados
        }
    }

}
