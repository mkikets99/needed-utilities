package com.github.mkikets99.neededutility.tools.speed

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SpeedTimeDistanceTest {

    private val delta = 1e-6

    @Test
    fun solveSpeed() {
        val result = SpeedTimeDistance.solve(distance = 100.0, timeHours = 2.0, speed = null)!!
        assertEquals(50.0, result.speed, delta)
    }

    @Test
    fun solveDistance() {
        val result = SpeedTimeDistance.solve(distance = null, timeHours = 2.0, speed = 50.0)!!
        assertEquals(100.0, result.distance, delta)
    }

    @Test
    fun solveTime() {
        val result = SpeedTimeDistance.solve(distance = 100.0, timeHours = null, speed = 25.0)!!
        assertEquals(4.0, result.timeHours, delta)
    }

    @Test
    fun needsExactlyTwoInputs() {
        assertNull(SpeedTimeDistance.solve(100.0, 2.0, 50.0))
        assertNull(SpeedTimeDistance.solve(100.0, null, null))
        assertNull(SpeedTimeDistance.solve(null, null, null))
    }

    @Test
    fun nonPositiveValuesRejected() {
        assertNull(SpeedTimeDistance.solve(0.0, 2.0, null))
        assertNull(SpeedTimeDistance.solve(-5.0, 2.0, null))
        assertNull(SpeedTimeDistance.solve(100.0, -1.0, null))
    }
}
