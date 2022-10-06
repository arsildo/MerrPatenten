package com.arsildo.merr_patenten.logic.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arsildo.merr_patenten.logic.constants.DATASTORE_KEY
import com.arsildo.merr_patenten.logic.constants.THEME_PREFERENCE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_KEY)
        val THEME_PREFERENCE_KEY = stringPreferencesKey(THEME_PREFERENCE)
    }

    val getThemePreference: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_PREFERENCE_KEY] ?: "light_mode"
    }

    suspend fun setThemePreference(themeName: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_PREFERENCE_KEY] = themeName
        }
    }

}