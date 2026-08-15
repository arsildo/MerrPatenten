package com.arsildo.merrpatenten.shared.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {
    object PreferencesKeys {
        val IMMERSIVE_MODE = booleanPreferencesKey("immersive_mode")
        val SAVE_STATS = booleanPreferencesKey("save_stats")
        val AUTOMATIC_COLOR_SCHEME = booleanPreferencesKey("automatic_color_scheme")
        val COLOR_SCHEME = booleanPreferencesKey("color_scheme")
        val DYNAMIC_COLOR_SCHEME = booleanPreferencesKey("dynamic_color_scheme")
    }

    val getImmersiveMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IMMERSIVE_MODE] ?: false
    }

    suspend fun setImmersiveMode(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.IMMERSIVE_MODE] = enabled }
    }

    val getSaveStats: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.SAVE_STATS] ?: true
    }

    suspend fun setSaveStats(save: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.SAVE_STATS] = save }
    }

    val getSystemColorScheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AUTOMATIC_COLOR_SCHEME] ?: true
    }

    suspend fun setFollowSystemColorScheme(automatic: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.AUTOMATIC_COLOR_SCHEME] = automatic }
    }

    val getColorScheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.COLOR_SCHEME] ?: false
    }

    suspend fun setColorScheme(darkMode: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.COLOR_SCHEME] = darkMode }
    }

    val getDynamicColorScheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DYNAMIC_COLOR_SCHEME] ?: true
    }

    suspend fun setDynamicColorScheme(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.DYNAMIC_COLOR_SCHEME] = enabled }
    }
}
