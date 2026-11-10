package com.github.mkikets99.neededutility.monetization

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

private val Context.proDataStore by preferencesDataStore(name = "pro")

/**
 * Global Pro state. Source of truth is the DataStore flag (survives offline
 * restarts); [MutableStateFlow] mirrors it for Compose. Purchase truth is
 * re-synced from Play Billing on startup (see [BillingManager]).
 */
object ProState {
    val isPro: MutableStateFlow<Boolean> = MutableStateFlow(false)
}

/**
 * Local persistence of the Pro flag (one-time purchase, non-consumable).
 */
class ProRepository(private val context: Context) {

    private val key = booleanPreferencesKey("is_pro")

    val isPro: Flow<Boolean> = context.proDataStore.data.map { it[key] ?: false }

    suspend fun setPro(value: Boolean) {
        ProState.isPro.value = value
        context.proDataStore.edit { it[key] = value }
    }
}
