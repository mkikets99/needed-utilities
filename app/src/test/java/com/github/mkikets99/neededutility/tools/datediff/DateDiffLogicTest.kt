package com.github.mkikets99.neededutility.tools.datediff

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DateDiffLogicTest {

    @Test
    fun simpleDays() {
        val result = DateDiffLogic.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31))
        assertEquals(30L, result.totalDays)
    }

    @Test
    fun yearMonthDayBreakdown() {
        val result = DateDiffLogic.between(LocalDate.of(2020, 1, 15), LocalDate.of(2026, 3, 20))
        assertEquals(6L, result.years)
        assertEquals(2L, result.months)
        assertEquals(5L, result.days)
    }

    @Test
    fun orderDoesNotMatter() {
        val a = DateDiffLogic.between(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 10))
        val b = DateDiffLogic.between(LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 1))
        assertEquals(a.totalDays, b.totalDays)
        assertEquals(a.days, b.days)
    }

    @Test
    fun sameDateIsZero() {
        val result = DateDiffLogic.between(LocalDate.of(2026, 7, 4), LocalDate.of(2026, 7, 4))
        assertEquals(0L, result.totalDays)
    }
}
