package com.github.mkikets99.neededutility.tools.qr

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

data class ScanEntry(
    val id: Long,
    val content: String,
    val at: Long,
)

private val Context.scanHistoryDataStore by preferencesDataStore(name = "scan_history")

/**
 * QR scan history stored as a JSON array in DataStore preferences.
 * Keeps the newest [MAX_ENTRIES] entries.
 */
class ScanHistoryRepository(private val context: Context) {

    companion object {
        const val MAX_ENTRIES = 50
    }

    private val key = stringPreferencesKey("entries_json")

    val entries: Flow<List<ScanEntry>> = context.scanHistoryDataStore.data.map { prefs ->
        parse(prefs[key])
    }

    suspend fun add(content: String) {
        context.scanHistoryDataStore.edit { prefs ->
            val current = parse(prefs[key])
            val newEntry = ScanEntry(
                id = System.currentTimeMillis(),
                content = content,
                at = System.currentTimeMillis(),
            )
            prefs[key] = serialize((listOf(newEntry) + current).take(MAX_ENTRIES))
        }
    }

    suspend fun clear() {
        context.scanHistoryDataStore.edit { it[key] = "[]" }
    }

    private fun serialize(entries: List<ScanEntry>): String {
        val array = JSONArray()
        entries.forEach { entry ->
            array.put(
                JSONObject()
                    .put("id", entry.id)
                    .put("content", entry.content)
                    .put("at", entry.at)
            )
        }
        return array.toString()
    }

    private fun parse(json: String?): List<ScanEntry> {
        if (json.isNullOrBlank()) return emptyList()
        return runCatching {
            val array = JSONArray(json)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        ScanEntry(
                            id = obj.optLong("id"),
                            content = obj.optString("content"),
                            at = obj.optLong("at"),
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }
}
