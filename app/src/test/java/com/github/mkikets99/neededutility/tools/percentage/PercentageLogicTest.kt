package com.github.mkikets99.neededutility.tools.percentage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PercentageLogicTest {

    private val delta = 1e-9

    @Test
    fun percentOf() {
        assertEquals(15.0, PercentageLogic.percentOf(15.0, 100.0), delta)
        assertEquals(250.0, PercentageLogic.percentOf(25.0, 1000.0), delta)
    }

    @Test
    fun whatPercentIs() {
        assertEquals(50.0, PercentageLogic.whatPercentIs(50.0, 100.0)!!, delta)
        assertEquals(20.0, PercentageLogic.whatPercentIs(1.0, 5.0)!!, delta)
    }

    @Test
    fun whatPercentWithZeroWholeIsNull() {
        assertNull(PercentageLogic.whatPercentIs(1.0, 0.0))
    }

    @Test
    fun percentChange() {
        assertEquals(50.0, PercentageLogic.percentChange(100.0, 150.0)!!, delta)
        assertEquals(-20.0, PercentageLogic.percentChange(100.0, 80.0)!!, delta)
    }

    @Test
    fun percentChangeFromZeroIsNull() {
        assertNull(PercentageLogic.percentChange(0.0, 10.0))
    }

    @Test
    fun addPercent() {
        assertEquals(115.0, PercentageLogic.addPercent(100.0, 15.0), delta)
    }
}
