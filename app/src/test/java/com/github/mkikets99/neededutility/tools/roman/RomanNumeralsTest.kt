package com.github.mkikets99.neededutility.tools.roman

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RomanNumeralsTest {

    @Test
    fun toRomanBasics() {
        assertEquals("I", RomanNumerals.toRoman(1))
        assertEquals("IV", RomanNumerals.toRoman(4))
        assertEquals("IX", RomanNumerals.toRoman(9))
        assertEquals("XLII", RomanNumerals.toRoman(42))
        assertEquals("MMXXVI", RomanNumerals.toRoman(2026))
        assertEquals("MMMCMXCIX", RomanNumerals.toRoman(3999))
    }

    @Test
    fun toRomanRange() {
        assertNull(RomanNumerals.toRoman(0))
        assertNull(RomanNumerals.toRoman(4000))
        assertNull(RomanNumerals.toRoman(-5))
    }

    @Test
    fun fromRomanBasics() {
        assertEquals(42, RomanNumerals.fromRoman("XLII"))
        assertEquals(2026, RomanNumerals.fromRoman("mmxxvi"))
        assertEquals(3999, RomanNumerals.fromRoman("MMMCMXCIX"))
    }

    @Test
    fun fromRomanRejectsMalformed() {
        assertNull(RomanNumerals.fromRoman("IIII"))
        assertNull(RomanNumerals.fromRoman("ABC"))
        assertNull(RomanNumerals.fromRoman(""))
    }

    @Test
    fun roundTrip() {
        for (value in listOf(1, 4, 9, 49, 50, 3888, 3999)) {
            assertEquals(value, RomanNumerals.fromRoman(RomanNumerals.toRoman(value)!!))
        }
    }
}
