package com.github.mkikets99.neededutility.tools.age

import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

data class AgeResult(
    val years: Long,
    val months: Long,
    val days: Long,
    val totalDays: Long,
    val nextBirthdayAge: Long,
    val daysUntilNextBirthday: Long,
)

/**
 * Age and birthday math. Pure — covered by unit tests.
 */
object AgeCalculator {

    fun calculate(birthDate: LocalDate, today: LocalDate): AgeResult? {
        if (birthDate > today) return null
        val period = Period.between(birthDate, today)

        var nextBirthday = birthDate.withYear(today.year)
        if (nextBirthday < today) nextBirthday = nextBirthday.plusYears(1)
        val nextAge = ChronoUnit.YEARS.between(birthDate, nextBirthday)

        return AgeResult(
            years = period.years.toLong(),
            months = period.months.toLong(),
            days = period.days.toLong(),
            totalDays = ChronoUnit.DAYS.between(birthDate, today),
            nextBirthdayAge = nextAge,
            daysUntilNextBirthday = ChronoUnit.DAYS.between(today, nextBirthday),
        )
    }
}
