package com.github.mkikets99.neededutility.tools.bmi

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BmiCalculatorTest {

    private val delta = 0.05

    @Test
    fun normalWeight() {
        val result = BmiCalculator.calculate(180.0, 75.0)!!
        assertEquals(23.1, result.bmi, delta)
        assertEquals("Normal weight", result.category)
    }

    @Test
    fun underweight() {
        val result = BmiCalculator.calculate(180.0, 50.0)!!
        assertEquals("Underweight", result.category)
    }

    @Test
    fun overweight() {
        val result = BmiCalculator.calculate(170.0, 80.0)!!
        assertEquals("Overweight", result.category)
    }

    @Test
    fun obese() {
        val result = BmiCalculator.calculate(160.0, 100.0)!!
        assertEquals("Obese", result.category)
    }

    @Test
    fun healthyRangeMatchesHeight() {
        val result = BmiCalculator.calculate(180.0, 75.0)!!
        assertEquals(59.9, result.healthyWeightFrom, 0.1)
        assertEquals(80.7, result.healthyWeightTo, 0.1)
    }

    @Test
    fun invalidInputsReturnNull() {
        assertNull(BmiCalculator.calculate(0.0, 70.0))
        assertNull(BmiCalculator.calculate(180.0, -5.0))
    }
}
