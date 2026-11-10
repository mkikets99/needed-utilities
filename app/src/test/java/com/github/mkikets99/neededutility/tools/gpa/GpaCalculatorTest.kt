package com.github.mkikets99.neededutility.tools.gpa

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GpaCalculatorTest {

    private val delta = 1e-6

    @Test
    fun straightAs() {
        val gpa = GpaCalculator.calculate(listOf("A" to 3.0, "A-" to 3.0))!!
        assertEquals((4.0 * 3.0 + 3.7 * 3.0) / 6.0, gpa, delta)
    }

    @Test
    fun weightedByCredits() {
        val gpa = GpaCalculator.calculate(listOf("B" to 1.0, "A" to 3.0))!!
        assertEquals((3.0 * 1.0 + 4.0 * 3.0) / 4.0, gpa, delta)
    }

    @Test
    fun gradeLetterCaseIgnored() {
        val gpa = GpaCalculator.calculate(listOf("a" to 1.0))!!
        assertEquals(4.0, gpa, delta)
    }

    @Test
    fun invalidGrade() {
        assertNull(GpaCalculator.calculate(listOf("Z" to 1.0)))
    }

    @Test
    fun invalidCredits() {
        assertNull(GpaCalculator.calculate(listOf("A" to 0.0)))
    }

    @Test
    fun emptyCourseListIsNull() {
        assertNull(GpaCalculator.calculate(emptyList()))
    }
}
