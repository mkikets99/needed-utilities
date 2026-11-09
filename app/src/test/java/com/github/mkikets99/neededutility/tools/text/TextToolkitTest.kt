package com.github.mkikets99.neededutility.tools.text

import org.junit.Assert.assertEquals
import org.junit.Test

class TextToolkitTest {

    @Test
    fun counts() {
        val text = "hello world\nsecond line"
        assertEquals(23, TextToolkit.charCount(text))
        assertEquals(20, TextToolkit.charCountNoSpaces(text))
        assertEquals(4, TextToolkit.wordCount(text))
        assertEquals(2, TextToolkit.lineCount(text))
    }

    @Test
    fun emptyText() {
        assertEquals(0, TextToolkit.wordCount("   "))
        assertEquals(0, TextToolkit.lineCount(""))
    }

    @Test
    fun caseTransforms() {
        assertEquals("HELLO WORLD", TextToolkit.upper("hello world"))
        assertEquals("hello world", TextToolkit.lower("HELLO WORLD"))
        assertEquals("Hello World", TextToolkit.titleCase("hello world"))
    }

    @Test
    fun sentenceCase() {
        assertEquals("Hello world. This is fine!", TextToolkit.sentenceCase("hello world. this is fine!"))
    }

    @Test
    fun reverse() {
        assertEquals("cba", TextToolkit.reverse("abc"))
    }

    @Test
    fun trimLines() {
        assertEquals("a\nb", TextToolkit.trimLines("  a  \n  b  "))
    }
}
