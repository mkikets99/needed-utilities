package com.github.mkikets99.neededutility.tools.loan

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class LoanCalculatorTest {

    private val delta = 0.01

    @Test
    fun zeroInterestLoan() {
        val result = LoanCalculator.calculate(1200.0, 0.0, 12)!!
        assertEquals(100.0, result.monthlyPayment, delta)
        assertEquals(1200.0, result.totalPayment, delta)
        assertEquals(0.0, result.totalInterest, delta)
    }

    @Test
    fun amortizedLoan() {
        // Classic example: 100 000 at 12% over 120 months → EMI ≈ 1434.71
        val result = LoanCalculator.calculate(100_000.0, 12.0, 120)!!
        assertEquals(1434.71, result.monthlyPayment, delta)
        assertEquals(172165.2, result.totalPayment, 0.1)
        assertEquals(72165.2, result.totalInterest, 0.1)
    }

    @Test
    fun invalidInputsReturnNull() {
        assertNull(LoanCalculator.calculate(0.0, 5.0, 12))
        assertNull(LoanCalculator.calculate(1000.0, 5.0, 0))
        assertNull(LoanCalculator.calculate(1000.0, -1.0, 12))
    }

    @Test
    fun resultIsAlwaysPositive() {
        val result = LoanCalculator.calculate(5000.0, 7.5, 36)
        assertNotNull(result)
        assert(result!!.monthlyPayment > 0.0)
        assert(result.totalInterest > 0.0)
    }
}
