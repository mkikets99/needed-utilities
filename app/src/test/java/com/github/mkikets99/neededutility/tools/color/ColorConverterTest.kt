package com.github.mkikets99.neededutility.tools.color

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ColorConverterTest {

    @Test
    fun fromHexWithHash() {
        val color = ColorConverter.fromHex("#00696E")!!
        assertEquals(0, color.red)
        assertEquals(105, color.green)
        assertEquals(110, color.blue)
    }

    @Test
    fun fromHexWithoutHashAndLowerCase() {
        val color = ColorConverter.fromHex("ff8000")!!
        assertEquals(255, color.red)
        assertEquals(128, color.green)
        assertEquals(0, color.blue)
    }

    @Test
    fun toHexRoundTrip() {
        assertEquals("#FF8000", ColorConverter.toHex(255, 128, 0))
        val color = ColorConverter.fromHex("#AABBCC")!!
        assertEquals("#AABBCC", color.hex)
    }

    @Test
    fun invalidHex() {
        assertNull(ColorConverter.fromHex("#12345"))
        assertNull(ColorConverter.fromHex("#GGGGGG"))
        assertNull(ColorConverter.fromHex(""))
    }

    @Test
    fun clampsOutOfRangeChannels() {
        assertEquals("#FF0000", ColorConverter.toHex(300, -5, 0))
    }
}
