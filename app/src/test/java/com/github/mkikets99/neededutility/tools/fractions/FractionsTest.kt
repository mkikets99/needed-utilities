package com.github.mkikets99.neededutility.tools.fractions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FractionsTest {

    @Test
    fun parseFraction() {
        val fraction = Fractions.parse("3/4")!!
        assertEquals(3L, fraction.numerator)
        assertEquals(4L, fraction.denominator)
    }

    @Test
    fun parseSimplifies() {
        val fraction = Fractions.parse("6/8")!!
        assertEquals(3L, fraction.numerator)
        assertEquals(4L, fraction.denominator)
    }

    @Test
    fun parseDecimal() {
        val fraction = Fractions.parse("0.75")!!
        assertEquals(3L, fraction.numerator)
        assertEquals(4L, fraction.denominator)
    }

    @Test
    fun parseRecurringDecimal() {
        val fraction = Fractions.parse("0.333333")!!
        assertEquals(1L, fraction.numerator)
        assertEquals(3L, fraction.denominator)
    }

    @Test
    fun invalidInput() {
        assertNull(Fractions.parse("1/0"))
        assertNull(Fractions.parse("abc"))
        assertNull(Fractions.parse(""))
        assertNull(Fractions.parse("1/2/3"))
    }

    @Test
    fun negativeFractions() {
        val fraction = Fractions.parse("-3/4")!!
        assertEquals(-3L, fraction.numerator)
        assertEquals(4L, fraction.denominator)
    }

    @Test
    fun decimalValue() {
        assertEquals(0.75, Fractions.parse("3/4")!!.decimal, 1e-9)
    }
}
