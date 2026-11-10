package com.github.mkikets99.neededutility.tools.metronome

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TapTempoTest {

    private val delta = 0.01

    @Test
    fun fourEvenTapsGive120Bpm() {
        val bpm = TapTempo.bpmFromTaps(listOf(0L, 500L, 1000L, 1500L))!!
        assertEquals(120.0, bpm, delta)
    }

    @Test
    fun staleTapsAreIgnored() {
        // Old taps at 0/100 are stale relative to the fresh 5000..6000 run.
        val bpm = TapTempo.bpmFromTaps(listOf(0L, 100L, 5000L, 5500L, 6000L))!!
        assertEquals(120.0, bpm, delta)
    }

    @Test
    fun needsThreeTaps() {
        assertNull(TapTempo.bpmFromTaps(emptyList()))
        assertNull(TapTempo.bpmFromTaps(listOf(0L)))
        assertNull(TapTempo.bpmFromTaps(listOf(0L, 500L)))
    }

    @Test
    fun clampsToValidRange() {
        // Extremely fast taps clamp to MAX_BPM; extremely slow to MIN_BPM.
        val fast = TapTempo.bpmFromTaps(listOf(0L, 1L, 2L, 3L))!!
        assertEquals(TapTempo.MAX_BPM, fast, delta)
        // 2500ms intervals -> 24 BPM, clamped up to MIN_BPM (30).
        val slow = TapTempo.bpmFromTaps(listOf(0L, 2500L, 5000L))!!
        assertEquals(TapTempo.MIN_BPM, slow, delta)
    }
}
