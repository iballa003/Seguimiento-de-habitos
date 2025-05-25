package org.iesharia.seguimientodehabitos.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ThemePreferenceManager {
    private val Context.dataStore by preferencesDataStore(name = "settings")
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    suspend fun setDarkMode(context: Context, isDark: Boolean) {
        context.dataStore.edit { it[DARK_MODE_KEY] = isDark }
    }

    fun getDarkModeFlow(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { it[DARK_MODE_KEY] ?: false }
    }
}


