package com.github.mkikets99.neededutility.tools.prime

import kotlin.math.sqrt

/**
 * Primality testing and prime factorization. Pure — covered by unit tests.
 */
object PrimeTools {

    fun isPrime(value: Long): Boolean {
        if (value < 2) return false
        if (value < 4) return true
        if (value % 2 == 0L || value % 3 == 0L) return value == 2L || value == 3L
        var i = 5L
        while (i <= sqrt(value.toDouble())) {
            if (value % i == 0L || value % (i + 2) == 0L) return false
            i += 6
        }
        return true
    }

    /** Prime factors with repetition, ascending (12 → [2, 2, 3]). Empty for values < 2. */
    fun factorize(value: Long): List<Long> {
        if (value < 2) return emptyList()
        var remaining = value
        val factors = mutableListOf<Long>()
        var divisor = 2L
        while (divisor <= sqrt(remaining.toDouble())) {
            while (remaining % divisor == 0L) {
                factors.add(divisor)
                remaining /= divisor
            }
            divisor = if (divisor == 2L) 3L else divisor + 2
        }
        if (remaining > 1) factors.add(remaining)
        return factors
    }
}
