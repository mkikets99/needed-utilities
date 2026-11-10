package com.github.mkikets99.neededutility.tools.words

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NumberToWordsTest {

    @Test
    fun basics() {
        assertEquals("zero", NumberToWords.convert(0))
        assertEquals("seven", NumberToWords.convert(7))
        assertEquals("nineteen", NumberToWords.convert(19))
    }

    @Test
    fun tensAndHyphenation() {
        assertEquals("twenty", NumberToWords.convert(20))
        assertEquals("twenty-one", NumberToWords.convert(21))
        assertEquals("forty-two", NumberToWords.convert(42))
    }

    @Test
    fun hundreds() {
        assertEquals("one hundred", NumberToWords.convert(100))
        assertEquals("one hundred fifteen", NumberToWords.convert(115))
        assertEquals("nine hundred ninety-nine", NumberToWords.convert(999))
    }

    @Test
    fun thousands() {
        assertEquals("one thousand one", NumberToWords.convert(1001))
        assertEquals(
            "twenty-one thousand two hundred thirty-four",
            NumberToWords.convert(21_234),
        )
    }

    @Test
    fun millions() {
        assertEquals("one million one", NumberToWords.convert(1_000_001))
        assertEquals(
            "nine hundred ninety-nine million nine hundred ninety-nine thousand " +
                "nine hundred ninety-nine",
            NumberToWords.convert(999_999_999),
        )
    }

    @Test
    fun negative() {
        assertEquals("minus five", NumberToWords.convert(-5))
    }

    @Test
    fun outOfRange() {
        assertNull(NumberToWords.convert(1_000_000_000))
        assertNull(NumberToWords.convert(-1_000_000_000))
    }
}
