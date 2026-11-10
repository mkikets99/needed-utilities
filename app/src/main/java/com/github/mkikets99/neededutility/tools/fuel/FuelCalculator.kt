package com.github.mkikets99.neededutility.tools.fuel

data class FuelResult(
    val litersUsed: Double,
    val consumptionL100: Double,
    val kmPerLiter: Double,
    val totalCost: Double,
    val costPerKm: Double,
)

/**
 * Fuel consumption and trip cost math. Pure — covered by unit tests.
 */
object FuelCalculator {

    fun calculate(
        distanceKm: Double,
        consumptionL100: Double,
        pricePerLiter: Double,
    ): FuelResult? {
        if (distanceKm <= 0.0 || consumptionL100 <= 0.0 || pricePerLiter < 0.0) return null
        val liters = distanceKm * consumptionL100 / 100.0
        val cost = liters * pricePerLiter
        return FuelResult(
            litersUsed = liters,
            consumptionL100 = consumptionL100,
            kmPerLiter = 100.0 / consumptionL100,
            totalCost = cost,
            costPerKm = cost / distanceKm,
        )
    }
}
