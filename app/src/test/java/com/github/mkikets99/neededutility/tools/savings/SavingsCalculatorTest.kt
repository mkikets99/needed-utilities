package com.github.mkikets99.neededutility.tools.savings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SavingsCalculatorTest {

    private val delta = 0.01

    @Test
    fun zeroInterest() {
        val result = SavingsCalculator.calculate(1000.0, 0.0, 2, 100.0)!!
        assertEquals(3400.0, result.futureValue, delta)
        assertEquals(3400.0, result.totalContributed, delta)
        assertEquals(0.0, result.interestEarned, delta)
    }

    @Test
    fun compoundGrowth() {
        // 10 000 at 6% for 10 years, no deposits
        val result = SavingsCalculator.calculate(10_000.0, 6.0, 10, 0.0)!!
        assertEquals(18193.97, result.futureValue, delta)
        assertEquals(10_000.0, result.totalContributed, delta)
        assertEquals(8193.97, result.interestEarned, delta)
    }

    @Test
    fun withMonthlyDeposits() {
        val result = SavingsCalculator.calculate(0.0, 6.0, 10, 100.0)!!
        assertEquals(16_387.93, result.futureValue, delta)
        assertEquals(12_000.0, result.totalContributed, delta)
    }

    @Test
    fun invalidInputs() {
        assertNull(SavingsCalculator.calculate(-1.0, 5.0, 5, 0.0))
        assertNull(SavingsCalculator.calculate(100.0, 5.0, 0, 0.0))
        assertNull(SavingsCalculator.calculate(0.0, 5.0, 5, 0.0))
    }
}
