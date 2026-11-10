package com.github.mkikets99.neededutility.tools.countdown

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

data class CountdownEvent(
    val id: Long,
    val name: String,
    val date: LocalDate,
)

private val Context.countdownDataStore by preferencesDataStore(name = "countdown_events")

/**
 * Named countdown events stored as a JSON array in DataStore preferences.
 */
class CountdownEventsRepository(private val context: Context) {

    private val key = stringPreferencesKey("events_json")

    val events: Flow<List<CountdownEvent>> = context.countdownDataStore.data.map { prefs ->
        parse(prefs[key]).sortedBy { it.date }
    }

    suspend fun add(name: String, date: LocalDate) {
        context.countdownDataStore.edit { prefs ->
            val current = parse(prefs[key])
            val newEvent = CountdownEvent(
                id = System.currentTimeMillis(),
                name = name,
                date = date,
            )
            prefs[key] = serialize(current + newEvent)
        }
    }

    suspend fun delete(id: Long) {
        context.countdownDataStore.edit { prefs ->
            prefs[key] = serialize(parse(prefs[key]).filterNot { it.id == id })
        }
    }

    private fun serialize(events: List<CountdownEvent>): String {
        val array = JSONArray()
        events.forEach { event ->
            array.put(
                JSONObject()
                    .put("id", event.id)
                    .put("name", event.name)
                    .put("date", event.date.toString())
            )
        }
        return array.toString()
    }

    private fun parse(json: String?): List<CountdownEvent> {
        if (json.isNullOrBlank()) return emptyList()
        return runCatching {
            val array = JSONArray(json)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        CountdownEvent(
                            id = obj.optLong("id"),
                            name = obj.optString("name"),
                            date = LocalDate.parse(obj.optString("date")),
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }
}
