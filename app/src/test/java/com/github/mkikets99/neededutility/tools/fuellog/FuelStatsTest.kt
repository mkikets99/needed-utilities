package com.github.mkikets99.neededutility.tools.fuellog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FuelStatsTest {

    private val delta = 1e-6

    private val fillUps = listOf(
        FillUp(1, odometerKm = 1000.0, liters = 40.0, pricePerLiter = 1.5),
        FillUp(2, odometerKm = 1500.0, liters = 35.0, pricePerLiter = 1.6),
        FillUp(3, odometerKm = 2000.0, liters = 38.0, pricePerLiter = 1.55),
    )

    @Test
    fun consumptionExcludesFirstFillUp() {
        val stats = FuelStats.compute(fillUps)!!
        assertEquals(7.3, stats.avgConsumptionL100!!, delta)
        assertEquals(1000.0, stats.totalDistance, delta)
    }

    @Test
    fun totals() {
        val stats = FuelStats.compute(fillUps)!!
        assertEquals(113.0, stats.totalLiters, delta)
        assertEquals(174.9, stats.totalCost, delta)
        assertEquals(174.9 / 113.0, stats.avgPricePerLiter!!, delta)
    }

    @Test
    fun singleFillUpHasNoConsumption() {
        val stats = FuelStats.compute(listOf(fillUps.first()))!!
        assertNull(stats.avgConsumptionL100)
    }

    @Test
    fun emptyLogIsNull() {
        assertNull(FuelStats.compute(emptyList()))
    }

    @Test
    fun unsortedInputIsHandled() {
        val stats = FuelStats.compute(fillUps.reversed())!!
        assertEquals(7.3, stats.avgConsumptionL100!!, delta)
    }
}
