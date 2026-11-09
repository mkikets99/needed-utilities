package com.github.mkikets99.neededutility.tools.notes

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

data class Note(
    val id: Long,
    val title: String,
    val body: String,
    val updatedAt: Long,
)

private val Context.notesDataStore by preferencesDataStore(name = "notes")

/**
 * Stores notes as a JSON array in DataStore preferences.
 * Zero extra dependencies; suitable for small note lists.
 */
class NotesRepository(private val context: Context) {

    private val key = stringPreferencesKey("notes_json")

    val notes: Flow<List<Note>> = context.notesDataStore.data.map { prefs ->
        parse(prefs[key]).sortedByDescending { it.updatedAt }
    }

    suspend fun upsert(note: Note) {
        context.notesDataStore.edit { prefs ->
            val current = parse(prefs[key]).filterNot { it.id == note.id }
            prefs[key] = serialize(current + note)
        }
    }

    suspend fun delete(id: Long) {
        context.notesDataStore.edit { prefs ->
            prefs[key] = serialize(parse(prefs[key]).filterNot { it.id == id })
        }
    }

    private fun serialize(notes: List<Note>): String {
        val array = JSONArray()
        notes.forEach { note ->
            array.put(
                JSONObject()
                    .put("id", note.id)
                    .put("title", note.title)
                    .put("body", note.body)
                    .put("updatedAt", note.updatedAt)
            )
        }
        return array.toString()
    }

    private fun parse(json: String?): List<Note> {
        if (json.isNullOrBlank()) return emptyList()
        return runCatching {
            val array = JSONArray(json)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        Note(
                            id = obj.getLong("id"),
                            title = obj.optString("title"),
                            body = obj.optString("body"),
                            updatedAt = obj.optLong("updatedAt"),
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }
}
