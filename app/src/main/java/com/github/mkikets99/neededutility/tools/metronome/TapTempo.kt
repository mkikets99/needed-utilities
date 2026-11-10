package com.github.mkikets99.neededutility.tools.metronome

/**
 * Tap tempo math: BPM from a list of tap timestamps (ms, monotonic).
 * Pure — covered by unit tests.
 */
object TapTempo {

    const val MIN_BPM = 30.0
    const val MAX_BPM = 240.0

    private const val STALE_WINDOW_MS = 5000L

    /**
     * BPM from at least 3 taps; taps older than [STALE_WINDOW_MS] before the
     * last tap are ignored. Null when there are too few usable taps.
     */
    fun bpmFromTaps(tapTimestampsMs: List<Long>): Double? {
        val last = tapTimestampsMs.lastOrNull() ?: return null
        val fresh = tapTimestampsMs.takeLast(6).filter { last - it <= STALE_WINDOW_MS }
        if (fresh.size < 3) return null

        val intervals = fresh.zipWithNext { a, b -> (b - a).toDouble() }
        val average = intervals.average()
        if (average <= 0.0) return null

        return (60_000.0 / average).coerceIn(MIN_BPM, MAX_BPM)
    }
}
