package com.github.mkikets99.neededutility.tools.age

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class AgeCalculatorTest {

    @Test
    fun exactAge() {
        val result = AgeCalculator.calculate(
            LocalDate.of(2000, 6, 15),
            LocalDate.of(2026, 6, 15),
        )!!
        assertEquals(26L, result.years)
        assertEquals(0L, result.months)
        assertEquals(0L, result.days)
        assertEquals(0L, result.daysUntilNextBirthday)
        assertEquals(26L, result.nextBirthdayAge)
    }

    @Test
    fun ageWithRemainder() {
        val result = AgeCalculator.calculate(
            LocalDate.of(2000, 1, 1),
            LocalDate.of(2026, 3, 5),
        )!!
        assertEquals(26L, result.years)
        assertEquals(2L, result.months)
        assertEquals(4L, result.days)
    }

    @Test
    fun nextBirthdayCountdown() {
        val result = AgeCalculator.calculate(
            LocalDate.of(1990, 12, 31),
            LocalDate.of(2026, 1, 1),
        )!!
        assertEquals(364L, result.daysUntilNextBirthday)
        assertEquals(36L, result.nextBirthdayAge)
    }

    @Test
    fun futureBirthDateIsNull() {
        assertNull(AgeCalculator.calculate(LocalDate.of(2030, 1, 1), LocalDate.of(2026, 1, 1)))
    }

    @Test
    fun totalDaysLived() {
        val result = AgeCalculator.calculate(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 1, 11),
        )!!
        assertEquals(10L, result.totalDays)
    }
}
