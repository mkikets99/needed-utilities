package com.github.mkikets99.neededutility.tools.bases

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BaseConverterTest {

    @Test
    fun parseDecimal() {
        assertEquals(255L, BaseConverter.parse("255", 10))
    }

    @Test
    fun parseHex() {
        assertEquals(255L, BaseConverter.parse("FF", 16))
        assertEquals(255L, BaseConverter.parse("ff", 16))
    }

    @Test
    fun parseBinary() {
        assertEquals(10L, BaseConverter.parse("1010", 2))
    }

    @Test
    fun parseInvalidReturnsNull() {
        assertNull(BaseConverter.parse("xyz", 10))
        assertNull(BaseConverter.parse("2", 2))
        assertNull(BaseConverter.parse("", 10))
    }

    @Test
    fun parseNegativeReturnsNull() {
        assertNull(BaseConverter.parse("-5", 10))
    }

    @Test
    fun formatAllRadices() {
        assertEquals("11111111", BaseConverter.format(255L, 2))
        assertEquals("377", BaseConverter.format(255L, 8))
        assertEquals("255", BaseConverter.format(255L, 10))
        assertEquals("FF", BaseConverter.format(255L, 16))
    }

    @Test
    fun roundTrip() {
        val value = 123456789L
        for (radix in BaseConverter.SUPPORTED_RADICES) {
            assertEquals(value, BaseConverter.parse(BaseConverter.format(value, radix), radix))
        }
    }
}
