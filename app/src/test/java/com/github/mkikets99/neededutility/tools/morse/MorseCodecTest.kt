package com.github.mkikets99.neededutility.tools.morse

import org.junit.Assert.assertEquals
import org.junit.Test

class MorseCodecTest {

    @Test
    fun encodeSos() {
        assertEquals("... --- ...", MorseCodec.encode("SOS"))
    }

    @Test
    fun encodeWords() {
        assertEquals(".... . .-.. .-.. --- / .-- --- .-. .-.. -..", MorseCodec.encode("hello world"))
    }

    @Test
    fun decodeSos() {
        assertEquals("SOS", MorseCodec.decode("... --- ..."))
    }

    @Test
    fun roundTrip() {
        val original = "HELLO WORLD 123"
        val decoded = MorseCodec.decode(MorseCodec.encode(original))
        assertEquals(original, decoded)
    }

    @Test
    fun unknownCharactersAreSkipped() {
        assertEquals(".-", MorseCodec.encode("a#"))
    }

    @Test
    fun decodeUnknownCodesAreSkipped() {
        assertEquals("A", MorseCodec.decode(".- .-..-"))
    }
}
