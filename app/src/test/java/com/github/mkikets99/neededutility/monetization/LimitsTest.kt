package com.github.mkikets99.neededutility.monetization

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LimitsTest {

    @Test
    fun freeTierAllowsUpToLimit() {
        assertTrue(Limits.canAdd(0, isPro = false, limit = 25))
        assertTrue(Limits.canAdd(24, isPro = false, limit = 25))
        assertFalse(Limits.canAdd(25, isPro = false, limit = 25))
    }

    @Test
    fun proIgnoresCaps() {
        assertTrue(Limits.canAdd(1_000_000, isPro = true, limit = 25))
    }

    @Test
    fun capsMatchAgreedValues() {
        // User decision 2026-11-10: "generous caps" = 25/25/100/100
        assertEquals(25, Limits.MAX_NOTES)
        assertEquals(25, Limits.MAX_EVENTS)
        assertEquals(100, Limits.MAX_FUEL_ENTRIES)
        assertEquals(100, Limits.MAX_TRIP_ENTRIES)
    }
}
