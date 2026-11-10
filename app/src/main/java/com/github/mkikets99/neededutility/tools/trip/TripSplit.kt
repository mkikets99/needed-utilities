package com.github.mkikets99.neededutility.tools.trip

data class ExpenseEntry(
    val id: Long,
    val label: String,
    val amount: Double,
)

/**
 * Trip expense totals and splitting. Pure — covered by unit tests.
 */
object TripSplit {

    fun total(expenses: List<ExpenseEntry>): Double =
        expenses.sumOf { it.amount.coerceAtLeast(0.0) }

    fun perPerson(total: Double, people: Int): Double? =
        if (people <= 0) null else total / people
}
