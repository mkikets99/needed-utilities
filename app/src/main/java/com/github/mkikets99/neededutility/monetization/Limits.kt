package com.github.mkikets99.neededutility.monetization

/**
 * Free-tier caps (user decision 2026-11-10: "generous caps").
 * Pro removes every cap. Pure — covered by unit tests.
 */
object Limits {

    const val MAX_NOTES = 25
    const val MAX_EVENTS = 25
    const val MAX_FUEL_ENTRIES = 100
    const val MAX_TRIP_ENTRIES = 100

    fun canAdd(currentCount: Int, isPro: Boolean, limit: Int): Boolean =
        isPro || currentCount < limit
}
