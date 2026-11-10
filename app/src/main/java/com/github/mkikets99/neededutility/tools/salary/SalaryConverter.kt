package com.github.mkikets99.neededutility.tools.salary

data class SalaryResult(
    val hourly: Double,
    val monthly: Double,
    val annual: Double,
)

/**
 * Hourly / monthly / annual salary conversion. Pure — covered by unit tests.
 */
object SalaryConverter {

    fun fromHourly(hourly: Double, hoursPerWeek: Double, weeksPerYear: Double): SalaryResult? {
        if (hourly <= 0.0) return null
        if (hoursPerWeek <= 0.0 || weeksPerYear <= 0.0) return null
        val annual = hourly * hoursPerWeek * weeksPerYear
        return SalaryResult(hourly = hourly, monthly = annual / 12.0, annual = annual)
    }

    fun fromMonthly(monthly: Double, hoursPerWeek: Double, weeksPerYear: Double): SalaryResult? {
        if (monthly <= 0.0) return null
        if (hoursPerWeek <= 0.0 || weeksPerYear <= 0.0) return null
        val annual = monthly * 12.0
        return SalaryResult(hourly = annual / (hoursPerWeek * weeksPerYear), monthly = monthly, annual = annual)
    }

    fun fromAnnual(annual: Double, hoursPerWeek: Double, weeksPerYear: Double): SalaryResult? {
        if (annual <= 0.0) return null
        if (hoursPerWeek <= 0.0 || weeksPerYear <= 0.0) return null
        return SalaryResult(
            hourly = annual / (hoursPerWeek * weeksPerYear),
            monthly = annual / 12.0,
            annual = annual,
        )
    }
}
