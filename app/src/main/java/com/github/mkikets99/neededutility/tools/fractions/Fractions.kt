package com.github.mkikets99.neededutility.tools.fractions

import kotlin.math.abs
import kotlin.math.floor

data class Fraction(val numerator: Long, val denominator: Long) {
    val decimal: Double
        get() = numerator.toDouble() / denominator
}

/**
 * Fraction parsing, simplification and decimal conversion.
 * Pure — covered by unit tests.
 */
object Fractions {

    private const val MAX_DENOMINATOR = 10_000L

    /** Parses "3/4" or a decimal like "0.75" into a simplified fraction. */
    fun parse(input: String): Fraction? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return null
        return if ('/' in trimmed) {
            val parts = trimmed.split('/')
            if (parts.size != 2) return null
            val num = parts[0].trim().toLongOrNull() ?: return null
            val den = parts[1].trim().toLongOrNull() ?: return null
            if (den == 0L) return null
            simplify(Fraction(num, den))
        } else {
            val value = trimmed.toDoubleOrNull() ?: return null
            fromDecimal(value)
        }
    }

    fun simplify(fraction: Fraction): Fraction {
        if (fraction.denominator == 0L) return fraction
        val divisor = gcd(abs(fraction.numerator), abs(fraction.denominator))
        var num = fraction.numerator / divisor
        var den = fraction.denominator / divisor
        if (den < 0) {
            num = -num
            den = -den
        }
        return Fraction(num, den)
    }

    /** Best rational approximation via continued fractions. */
    fun fromDecimal(value: Double, maxDenominator: Long = MAX_DENOMINATOR): Fraction {
        if (!value.isFinite()) return Fraction(0, 1)
        val negative = value < 0
        var x = abs(value)

        var h0 = 0L
        var h1 = 1L
        var k0 = 1L
        var k1 = 0L

        while (true) {
            val a = floor(x).toLong()
            val h2 = a * h1 + h0
            val k2 = a * k1 + k0
            if (k2 > maxDenominator || k2 < 0) break
            h0 = h1
            h1 = h2
            k0 = k1
            k1 = k2
            val frac = x - a
            if (frac < 1e-9) break
            x = 1.0 / frac
        }

        if (k1 == 0L) return Fraction(0, 1)
        val result = simplify(Fraction(h1, k1))
        return if (negative) Fraction(-result.numerator, result.denominator) else result
    }

    private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
}
