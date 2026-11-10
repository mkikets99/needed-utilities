package com.github.mkikets99.neededutility.tools.electricity

data class ElectricityResult(
    val kwhPerDay: Double,
    val kwhTotal: Double,
    val costPerDay: Double,
    val totalCost: Double,
)

/**
 * Appliance electricity cost math. Pure — covered by unit tests.
 */
object ElectricityCalculator {

    fun calculate(
        watts: Double,
        hoursPerDay: Double,
        days: Int,
        pricePerKwh: Double,
    ): ElectricityResult? {
        if (watts <= 0.0 || hoursPerDay <= 0.0 || hoursPerDay > 24.0) return null
        if (days <= 0 || pricePerKwh < 0.0) return null
        val kwhPerDay = watts / 1000.0 * hoursPerDay
        val kwhTotal = kwhPerDay * days
        val totalCost = kwhTotal * pricePerKwh
        return ElectricityResult(
            kwhPerDay = kwhPerDay,
            kwhTotal = kwhTotal,
            costPerDay = totalCost / days,
            totalCost = totalCost,
        )
    }
}
