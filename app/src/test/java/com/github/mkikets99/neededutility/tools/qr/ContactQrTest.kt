package com.github.mkikets99.neededutility.tools.qr

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ContactQrTest {

    @Test
    fun fullCard() {
        val card = ContactQr.build(
            name = "Ada Lovelace",
            phone = "+44 20 0000 0000",
            email = "ada@example.com",
            organization = "Analytical Engines",
        )!!
        assertTrue(card.startsWith("BEGIN:VCARD"))
        assertTrue(card.endsWith("END:VCARD"))
        assertTrue(card.contains("FN:Ada Lovelace"))
        assertTrue(card.contains("TEL:+44 20 0000 0000"))
        assertTrue(card.contains("EMAIL:ada@example.com"))
        assertTrue(card.contains("ORG:Analytical Engines"))
    }

    @Test
    fun minimalCardOmitsEmptyFields() {
        val card = ContactQr.build(name = "Solo")!!
        assertTrue(card.contains("FN:Solo"))
        assertTrue(!card.contains("TEL:"))
        assertTrue(!card.contains("EMAIL:"))
    }

    @Test
    fun nameIsRequired() {
        assertNull(ContactQr.build(name = "   "))
    }

    @Test
    fun versionIsDeclared() {
        assertTrue(ContactQr.build(name = "A")!!.contains("VERSION:3.0"))
    }
}
