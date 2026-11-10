package com.github.mkikets99.neededutility.tools.trip

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.tripDataStore by preferencesDataStore(name = "trip_expenses")

/**
 * Trip expenses stored as a JSON array in DataStore preferences.
 */
class TripExpensesRepository(private val context: Context) {

    private val key = stringPreferencesKey("expenses_json")

    val expenses: Flow<List<ExpenseEntry>> = context.tripDataStore.data.map { prefs ->
        parse(prefs[key])
    }

    suspend fun add(label: String, amount: Double) {
        context.tripDataStore.edit { prefs ->
            val current = parse(prefs[key])
            val entry = ExpenseEntry(
                id = System.currentTimeMillis(),
                label = label,
                amount = amount,
            )
            prefs[key] = serialize(current + entry)
        }
    }

    suspend fun delete(id: Long) {
        context.tripDataStore.edit { prefs ->
            prefs[key] = serialize(parse(prefs[key]).filterNot { it.id == id })
        }
    }

    suspend fun clear() {
        context.tripDataStore.edit { it[key] = "[]" }
    }

    private fun serialize(entries: List<ExpenseEntry>): String {
        val array = JSONArray()
        entries.forEach { entry ->
            array.put(
                JSONObject()
                    .put("id", entry.id)
                    .put("label", entry.label)
                    .put("amount", entry.amount)
            )
        }
        return array.toString()
    }

    private fun parse(json: String?): List<ExpenseEntry> {
        if (json.isNullOrBlank()) return emptyList()
        return runCatching {
            val array = JSONArray(json)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        ExpenseEntry(
                            id = obj.optLong("id"),
                            label = obj.optString("label"),
                            amount = obj.optDouble("amount"),
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }
}
