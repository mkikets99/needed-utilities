package com.github.mkikets99.neededutility.tools.salary

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SalaryConverterTest {

    private val delta = 0.01

    @Test
    fun fromHourly() {
        val result = SalaryConverter.fromHourly(25.0, 40.0, 52.0)!!
        assertEquals(25.0, result.hourly, delta)
        assertEquals(52_000.0, result.annual, delta)
        assertEquals(4333.33, result.monthly, delta)
    }

    @Test
    fun fromAnnual() {
        val result = SalaryConverter.fromAnnual(52_000.0, 40.0, 52.0)!!
        assertEquals(25.0, result.hourly, delta)
        assertEquals(4333.33, result.monthly, delta)
    }

    @Test
    fun fromMonthly() {
        val result = SalaryConverter.fromMonthly(1_000.0, 20.0, 50.0)!!
        assertEquals(12_000.0, result.annual, delta)
        assertEquals(12.0, result.hourly, delta)
    }

    @Test
    fun invalidInputs() {
        assertNull(SalaryConverter.fromHourly(0.0, 40.0, 52.0))
        assertNull(SalaryConverter.fromHourly(25.0, -1.0, 52.0))
        assertNull(SalaryConverter.fromMonthly(-5.0, 40.0, 52.0))
        assertNull(SalaryConverter.fromAnnual(100.0, 40.0, 0.0))
    }
}
