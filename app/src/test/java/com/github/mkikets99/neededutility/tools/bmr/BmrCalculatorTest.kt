package com.github.mkikets99.neededutility.tools.bmr

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BmrCalculatorTest {

    private val delta = 0.01

    @Test
    fun maleBmr() {
        // 10*80 + 6.25*180 - 5*30 + 5 = 800 + 1125 - 150 + 5 = 1780
        val result = BmrCalculator.calculate(BiologicalSex.MALE, 30, 180.0, 80.0, 1.2)!!
        assertEquals(1780.0, result.bmr, delta)
        assertEquals(2136.0, result.maintenance, delta)
    }

    @Test
    fun femaleBmr() {
        // 10*60 + 6.25*165 - 5*25 - 161 = 600 + 1031.25 - 125 - 161 = 1345.25
        val result = BmrCalculator.calculate(BiologicalSex.FEMALE, 25, 165.0, 60.0, 1.55)!!
        assertEquals(1345.25, result.bmr, delta)
    }

    @Test
    fun invalidInputs() {
        assertNull(BmrCalculator.calculate(BiologicalSex.MALE, 0, 180.0, 80.0, 1.2))
        assertNull(BmrCalculator.calculate(BiologicalSex.MALE, 30, -5.0, 80.0, 1.2))
        assertNull(BmrCalculator.calculate(BiologicalSex.MALE, 30, 180.0, 80.0, 0.0))
    }

    @Test
    fun activityLevelsCoverFiveEntries() {
        assertEquals(5, BmrCalculator.ACTIVITY_LEVELS.size)
    }
}
