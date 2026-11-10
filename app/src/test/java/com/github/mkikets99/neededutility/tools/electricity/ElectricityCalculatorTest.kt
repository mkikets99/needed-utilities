package com.github.mkikets99.neededutility.tools.electricity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ElectricityCalculatorTest {

    private val delta = 1e-6

    @Test
    fun basicAppliance() {
        // 100 W for 8 h/day over 30 days at 0.30/kWh
        val result = ElectricityCalculator.calculate(100.0, 8.0, 30, 0.30)!!
        assertEquals(0.8, result.kwhPerDay, delta)
        assertEquals(24.0, result.kwhTotal, delta)
        assertEquals(0.24, result.costPerDay, delta)
        assertEquals(7.2, result.totalCost, delta)
    }

    @Test
    fun invalidInputs() {
        assertNull(ElectricityCalculator.calculate(0.0, 8.0, 30, 0.3))
        assertNull(ElectricityCalculator.calculate(100.0, 25.0, 30, 0.3))
        assertNull(ElectricityCalculator.calculate(100.0, 8.0, 0, 0.3))
        assertNull(ElectricityCalculator.calculate(100.0, 8.0, 30, -0.1))
    }

    @Test
    fun freeElectricity() {
        val result = ElectricityCalculator.calculate(60.0, 1.0, 10, 0.0)!!
        assertEquals(0.0, result.totalCost, delta)
    }
}
