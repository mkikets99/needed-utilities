package com.github.mkikets99.neededutility.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.toolDataStore by preferencesDataStore(name = "tool_prefs")

/**
 * Light persistence for last-used values of individual tools (DataStore preferences).
 */
class ToolPreferences(private val context: Context) {

    private object Keys {
        val TIP_PERCENT = intPreferencesKey("tip_percent")
        val SPLIT_COUNT = intPreferencesKey("split_count")
        val CONVERTER_CATEGORY = stringPreferencesKey("converter_category")
        val CONVERTER_FROM = stringPreferencesKey("converter_from")
        val CONVERTER_TO = stringPreferencesKey("converter_to")
    }

    val tipPercent: Flow<Int> = context.toolDataStore.data.map { it[Keys.TIP_PERCENT] ?: 15 }
    val splitCount: Flow<Int> = context.toolDataStore.data.map { it[Keys.SPLIT_COUNT] ?: 1 }
    val converterCategory: Flow<String?> = context.toolDataStore.data.map { it[Keys.CONVERTER_CATEGORY] }
    val converterFromUnit: Flow<String?> = context.toolDataStore.data.map { it[Keys.CONVERTER_FROM] }
    val converterToUnit: Flow<String?> = context.toolDataStore.data.map { it[Keys.CONVERTER_TO] }

    suspend fun setTipPercent(percent: Int) {
        context.toolDataStore.edit { it[Keys.TIP_PERCENT] = percent.coerceIn(0, 100) }
    }

    suspend fun setSplitCount(count: Int) {
        context.toolDataStore.edit { it[Keys.SPLIT_COUNT] = count.coerceIn(1, 99) }
    }

    suspend fun setConverterUnits(category: String, from: String, to: String) {
        context.toolDataStore.edit {
            it[Keys.CONVERTER_CATEGORY] = category
            it[Keys.CONVERTER_FROM] = from
            it[Keys.CONVERTER_TO] = to
        }
    }

    suspend fun currentConverterUnits(): Triple<String?, String?, String?> {
        val prefs = context.toolDataStore.data.first()
        return Triple(
            prefs[Keys.CONVERTER_CATEGORY],
            prefs[Keys.CONVERTER_FROM],
            prefs[Keys.CONVERTER_TO],
        )
    }
}
