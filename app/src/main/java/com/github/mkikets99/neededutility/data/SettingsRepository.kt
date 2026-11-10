package com.github.mkikets99.neededutility.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode { SYSTEM, LIGHT, DARK }

enum class HomeLayoutMode { CARDS, LIST }

data class ThemeSettings(
    val mode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = true,
    val homeLayout: HomeLayoutMode = HomeLayoutMode.CARDS,
)

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

/**
 * Light persistence for app-level appearance settings (DataStore preferences).
 */
class SettingsRepository(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val HOME_LAYOUT = stringPreferencesKey("home_layout")
    }

    val settings: Flow<ThemeSettings> = context.settingsDataStore.data.map { prefs ->
        ThemeSettings(
            mode = prefs[Keys.THEME_MODE]
                ?.let { stored -> ThemeMode.entries.firstOrNull { it.name == stored } }
                ?: ThemeMode.SYSTEM,
            dynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: true,
            homeLayout = prefs[Keys.HOME_LAYOUT]
                ?.let { stored -> HomeLayoutMode.entries.firstOrNull { it.name == stored } }
                ?: HomeLayoutMode.CARDS,
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.settingsDataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.DYNAMIC_COLOR] = enabled }
    }

    suspend fun setHomeLayout(layout: HomeLayoutMode) {
        context.settingsDataStore.edit { it[Keys.HOME_LAYOUT] = layout.name }
    }
}
