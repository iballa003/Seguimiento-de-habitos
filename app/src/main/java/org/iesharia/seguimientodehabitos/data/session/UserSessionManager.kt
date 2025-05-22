package org.iesharia.seguimientodehabitos.data.session
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_prefs")

class UserSessionManager(private val context: Context) {
    companion object {
        val USER_ID_KEY = intPreferencesKey("user_id")
    }

    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    val userIdFlow: Flow<Int?> = context.dataStore.data
        .map { preferences -> preferences[USER_ID_KEY] }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
