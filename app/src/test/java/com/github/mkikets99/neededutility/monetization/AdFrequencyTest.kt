package com.github.mkikets99.neededutility.monetization

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AdFrequencyTest {

    @Before
    fun setUp() {
        AdFrequency.reset()
    }

    @Test
    fun showsOnFifthOpen() {
        repeat(4) { assertFalse(AdFrequency.onToolOpened()) }
        assertTrue(AdFrequency.onToolOpened())
    }

    @Test
    fun showsEveryFifthOpen() {
        val results = (1..15).map { AdFrequency.onToolOpened() }
        assertEquals(
            listOf(false, false, false, false, true, false, false, false, false, true,
                false, false, false, false, true),
            results,
        )
    }

    @Test
    fun reOpeningSameToolCounts() {
        // The rule is per open, not per distinct tool: five opens of the
        // SAME tool must trigger, exactly like five different tools.
        repeat(4) { assertFalse(AdFrequency.onToolOpened()) }
        assertTrue(AdFrequency.onToolOpened())
    }
}
