package com.github.mkikets99.neededutility.tools.prime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PrimeToolsTest {

    @Test
    fun primality() {
        assertTrue(PrimeTools.isPrime(2))
        assertTrue(PrimeTools.isPrime(3))
        assertTrue(PrimeTools.isPrime(97))
        assertFalse(PrimeTools.isPrime(1))
        assertFalse(PrimeTools.isPrime(0))
        assertFalse(PrimeTools.isPrime(100))
    }

    @Test
    fun factorize() {
        assertEquals(listOf(2L, 2L, 3L), PrimeTools.factorize(12))
        assertEquals(listOf(7L), PrimeTools.factorize(7))
        assertEquals(listOf(2L, 3L, 5L), PrimeTools.factorize(30))
    }

    @Test
    fun factorizeSmallValues() {
        assertEquals(emptyList<Long>(), PrimeTools.factorize(1))
        assertEquals(emptyList<Long>(), PrimeTools.factorize(0))
        assertEquals(emptyList<Long>(), PrimeTools.factorize(-5))
    }
}
