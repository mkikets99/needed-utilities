package com.github.mkikets99.neededutility.tools.datediff

import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

data class DateDiffResult(
    val totalDays: Long,
    val years: Long,
    val months: Long,
    val days: Long,
)

/**
 * Calendar-aware difference between two dates. Pure — covered by unit tests.
 */
object DateDiffLogic {

    fun between(from: LocalDate, to: LocalDate): DateDiffResult {
        val start = if (from <= to) from else to
        val end = if (from <= to) to else from
        val period = Period.between(start, end)
        return DateDiffResult(
            totalDays = ChronoUnit.DAYS.between(start, end),
            years = period.years.toLong(),
            months = period.months.toLong(),
            days = period.days.toLong(),
        )
    }
}
