package com.github.mkikets99.neededutility.tools.trip

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TripSplitTest {

    private val delta = 1e-6

    private val expenses = listOf(
        ExpenseEntry(1, "Fuel", 60.0),
        ExpenseEntry(2, "Hotel", 120.0),
        ExpenseEntry(3, "Food", 45.5),
    )

    @Test
    fun totalSumsExpenses() {
        assertEquals(225.5, TripSplit.total(expenses), delta)
    }

    @Test
    fun perPersonSplit() {
        assertEquals(56.375, TripSplit.perPerson(225.5, 4)!!, delta)
    }

    @Test
    fun invalidPeople() {
        assertNull(TripSplit.perPerson(100.0, 0))
    }

    @Test
    fun negativeAmountsIgnored() {
        val entries = listOf(ExpenseEntry(1, "Refund?", -10.0), ExpenseEntry(2, "Food", 30.0))
        assertEquals(30.0, TripSplit.total(entries), delta)
    }
}
