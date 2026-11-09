package com.github.mkikets99.neededutility.tools.unitprice

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UnitPriceCalculatorTest {

    private val delta = 1e-9

    @Test
    fun packAIsCheaper() {
        val result = UnitPriceCalculator.compare(Pack(5.0, 10.0), Pack(6.0, 10.0))!!
        assertEquals(0.5, result.unitPriceA, delta)
        assertEquals(0.6, result.unitPriceB, delta)
        assertTrue(result.cheaperIsA!!)
        assertEquals(16.666, result.savingsPercent, 0.001)
    }

    @Test
    fun packBIsCheaper() {
        val result = UnitPriceCalculator.compare(Pack(10.0, 100.0), Pack(2.0, 50.0))!!
        assertTrue(result.unitPriceB < result.unitPriceA)
        assertFalse(result.cheaperIsA!!)
    }

    @Test
    fun equalPacks() {
        val result = UnitPriceCalculator.compare(Pack(3.0, 6.0), Pack(6.0, 12.0))!!
        assertEquals(0.5, result.unitPriceA, delta)
        assertEquals(null, result.cheaperIsA)
        assertEquals(0.0, result.savingsPercent, delta)
    }

    @Test
    fun invalidInputs() {
        assertNull(UnitPriceCalculator.compare(Pack(5.0, 0.0), Pack(5.0, 5.0)))
        assertNull(UnitPriceCalculator.compare(Pack(-1.0, 5.0), Pack(5.0, 5.0)))
    }
}
