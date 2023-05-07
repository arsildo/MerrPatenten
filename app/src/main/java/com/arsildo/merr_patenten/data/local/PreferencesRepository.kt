package com.arsildo.merr_patenten.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.arsildo.merr_patenten.data.local.PreferencesRepository.PreferencesKeys.AUTOMATIC_COLOR_SCHEME
import com.arsildo.merr_patenten.data.local.PreferencesRepository.PreferencesKeys.COLOR_SCHEME
import com.arsildo.merr_patenten.data.local.PreferencesRepository.PreferencesKeys.CONFIRM_APP_EXIT
import com.arsildo.merr_patenten.data.local.PreferencesRepository.PreferencesKeys.DYNAMIC_COLOR_SCHEME
import com.arsildo.merr_patenten.data.local.PreferencesRepository.PreferencesKeys.SAVE_STATS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    object PreferencesKeys {

        val SAVE_STATS = booleanPreferencesKey("save_stats")
        val CONFIRM_APP_EXIT = booleanPreferencesKey("confirm_app_exit")

        val AUTOMATIC_COLOR_SCHEME = booleanPreferencesKey("automatic_color_scheme")
        val COLOR_SCHEME = booleanPreferencesKey("color_scheme")
        val DYNAMIC_COLOR_SCHEME = booleanPreferencesKey("dynamic_color_scheme")

    }


    // Save exam statistics
    val getSaveStats: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SAVE_STATS] ?: true
    }

    suspend fun setSaveStats(save: Boolean) {
        dataStore.edit { preferences -> preferences[SAVE_STATS] = save }
    }

    // Save exam statistics
    val getConfirmExitApp: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CONFIRM_APP_EXIT] ?: true
    }

    suspend fun setConfirmExitApp(confirm: Boolean) {
        dataStore.edit { preferences -> preferences[CONFIRM_APP_EXIT] = confirm }
    }


    // Follow System Color Scheme
    val getSystemColorScheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTOMATIC_COLOR_SCHEME] ?: true
    }

    suspend fun setFollowSystemColorScheme(automatic: Boolean) {
        dataStore.edit { preferences -> preferences[AUTOMATIC_COLOR_SCHEME] = automatic }
    }

    // Color Scheme
    val getColorScheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[COLOR_SCHEME] ?: false
    }

    suspend fun setColorScheme(darkMode: Boolean) {
        dataStore.edit { preferences -> preferences[COLOR_SCHEME] = darkMode }
    }

    // Dynamic Color Scheme [API 31+]
    val getDynamicColorScheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DYNAMIC_COLOR_SCHEME] ?: true
    }

    suspend fun setDynamicColorScheme(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[DYNAMIC_COLOR_SCHEME] = enabled }
    }


}