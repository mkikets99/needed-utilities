package com.github.mkikets99.neededutility.tools.fuellog

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.fuelLogDataStore by preferencesDataStore(name = "fuel_log")

/**
 * Fuel fill-ups stored as a JSON array in DataStore preferences.
 */
class FuelLogRepository(private val context: Context) {

    private val key = stringPreferencesKey("fillups_json")

    val fillUps: Flow<List<FillUp>> = context.fuelLogDataStore.data.map { prefs ->
        parse(prefs[key])
    }

    suspend fun add(odometerKm: Double, liters: Double, pricePerLiter: Double) {
        context.fuelLogDataStore.edit { prefs ->
            val current = parse(prefs[key])
            val entry = FillUp(
                id = System.currentTimeMillis(),
                odometerKm = odometerKm,
                liters = liters,
                pricePerLiter = pricePerLiter,
            )
            prefs[key] = serialize(current + entry)
        }
    }

    suspend fun delete(id: Long) {
        context.fuelLogDataStore.edit { prefs ->
            prefs[key] = serialize(parse(prefs[key]).filterNot { it.id == id })
        }
    }

    private fun serialize(entries: List<FillUp>): String {
        val array = JSONArray()
        entries.forEach { entry ->
            array.put(
                JSONObject()
                    .put("id", entry.id)
                    .put("odometerKm", entry.odometerKm)
                    .put("liters", entry.liters)
                    .put("pricePerLiter", entry.pricePerLiter)
            )
        }
        return array.toString()
    }

    private fun parse(json: String?): List<FillUp> {
        if (json.isNullOrBlank()) return emptyList()
        return runCatching {
            val array = JSONArray(json)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        FillUp(
                            id = obj.optLong("id"),
                            odometerKm = obj.optDouble("odometerKm"),
                            liters = obj.optDouble("liters"),
                            pricePerLiter = obj.optDouble("pricePerLiter"),
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }
}
