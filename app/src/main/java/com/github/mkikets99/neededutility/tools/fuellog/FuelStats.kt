package com.github.mkikets99.neededutility.tools.fuellog

data class FillUp(
    val id: Long,
    val odometerKm: Double,
    val liters: Double,
    val pricePerLiter: Double,
)

data class FuelStatsResult(
    val totalLiters: Double,
    val totalCost: Double,
    val totalDistance: Double,
    /** Null with fewer than two fill-ups. */
    val avgConsumptionL100: Double?,
    val avgPricePerLiter: Double?,
)

/**
 * Fuel log statistics. Consumption is computed over fill-ups after the first
 * (the first fill-up only establishes the baseline odometer).
 * Pure — covered by unit tests.
 */
object FuelStats {

    fun compute(fillUps: List<FillUp>): FuelStatsResult? {
        val sorted = fillUps.sortedBy { it.odometerKm }
        if (sorted.isEmpty()) return null

        val totalLiters = sorted.sumOf { it.liters }
        val totalCost = sorted.sumOf { it.liters * it.pricePerLiter }
        val distance = sorted.last().odometerKm - sorted.first().odometerKm

        val consumption = if (sorted.size >= 2 && distance > 0.0) {
            (totalLiters - sorted.first().liters) / distance * 100.0
        } else {
            null
        }

        return FuelStatsResult(
            totalLiters = totalLiters,
            totalCost = totalCost,
            totalDistance = distance,
            avgConsumptionL100 = consumption,
            avgPricePerLiter = if (totalLiters > 0.0) totalCost / totalLiters else null,
        )
    }
}
