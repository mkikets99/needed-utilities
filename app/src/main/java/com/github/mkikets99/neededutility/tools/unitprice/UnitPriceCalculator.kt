package com.github.mkikets99.neededutility.tools.unitprice

data class Pack(val price: Double, val quantity: Double)

data class UnitPriceResult(
    val unitPriceA: Double,
    val unitPriceB: Double,
    /** true = A is cheaper, false = B is cheaper, null = same price. */
    val cheaperIsA: Boolean?,
    /** How much cheaper the better option is, in percent (0 if equal). */
    val savingsPercent: Double,
)

/**
 * Compares two packs by price per unit. Pure — covered by unit tests.
 */
object UnitPriceCalculator {

    fun compare(a: Pack, b: Pack): UnitPriceResult? {
        if (a.price < 0.0 || b.price < 0.0) return null
        if (a.quantity <= 0.0 || b.quantity <= 0.0) return null

        val unitA = a.price / a.quantity
        val unitB = b.price / b.quantity

        val cheaper: Boolean? = when {
            unitA < unitB -> true
            unitB < unitA -> false
            else -> null
        }
        val savings = when (cheaper) {
            null -> 0.0
            true -> (unitB - unitA) / unitB * 100.0
            false -> (unitA - unitB) / unitA * 100.0
        }
        return UnitPriceResult(unitA, unitB, cheaper, savings)
    }
}
