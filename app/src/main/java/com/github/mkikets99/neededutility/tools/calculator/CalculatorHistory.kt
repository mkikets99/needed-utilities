package com.github.mkikets99.neededutility.tools.calculator

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

data class CalcEntry(
    val id: Long,
    val expression: String,
    val result: String,
    val at: Long,
)

private val Context.calcHistoryDataStore by preferencesDataStore(name = "calc_history")

/**
 * Calculator history stored as a JSON array in DataStore preferences.
 * Keeps the newest [MAX_ENTRIES] entries.
 */
class CalculatorHistoryRepository(private val context: Context) {

    companion object {
        const val MAX_ENTRIES = 20
    }

    private val key = stringPreferencesKey("entries_json")

    val entries: Flow<List<CalcEntry>> = context.calcHistoryDataStore.data.map { prefs ->
        parse(prefs[key])
    }

    suspend fun add(expression: String, result: String) {
        context.calcHistoryDataStore.edit { prefs ->
            val current = parse(prefs[key])
            val newEntry = CalcEntry(
                id = System.currentTimeMillis(),
                expression = expression,
                result = result,
                at = System.currentTimeMillis(),
            )
            prefs[key] = serialize((listOf(newEntry) + current).take(MAX_ENTRIES))
        }
    }

    suspend fun clear() {
        context.calcHistoryDataStore.edit { it[key] = "[]" }
    }

    private fun serialize(entries: List<CalcEntry>): String {
        val array = JSONArray()
        entries.forEach { entry ->
            array.put(
                JSONObject()
                    .put("id", entry.id)
                    .put("expression", entry.expression)
                    .put("result", entry.result)
                    .put("at", entry.at)
            )
        }
        return array.toString()
    }

    private fun parse(json: String?): List<CalcEntry> {
        if (json.isNullOrBlank()) return emptyList()
        return runCatching {
            val array = JSONArray(json)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        CalcEntry(
                            id = obj.optLong("id"),
                            expression = obj.optString("expression"),
                            result = obj.optString("result"),
                            at = obj.optLong("at"),
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }
}
