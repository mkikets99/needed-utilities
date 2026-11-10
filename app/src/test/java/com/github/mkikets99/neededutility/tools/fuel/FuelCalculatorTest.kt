package com.github.mkikets99.neededutility.tools.fuel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FuelCalculatorTest {

    private val delta = 1e-6

    @Test
    fun basicTrip() {
        val result = FuelCalculator.calculate(500.0, 8.0, 1.5)!!
        assertEquals(40.0, result.litersUsed, delta)
        assertEquals(60.0, result.totalCost, delta)
        assertEquals(0.12, result.costPerKm, delta)
        assertEquals(12.5, result.kmPerLiter, delta)
    }

    @Test
    fun invalidInputs() {
        assertNull(FuelCalculator.calculate(0.0, 8.0, 1.5))
        assertNull(FuelCalculator.calculate(100.0, 0.0, 1.5))
        assertNull(FuelCalculator.calculate(100.0, 8.0, -1.0))
    }

    @Test
    fun freeFuelIsZeroCost() {
        val result = FuelCalculator.calculate(100.0, 5.0, 0.0)!!
        assertEquals(0.0, result.totalCost, delta)
    }
}
