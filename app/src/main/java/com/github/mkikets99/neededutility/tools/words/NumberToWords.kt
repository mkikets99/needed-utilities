package com.github.mkikets99.neededutility.tools.words

/**
 * Integer to English words (American style, hyphenated tens).
 * Pure — covered by unit tests.
 */
object NumberToWords {

    private val ones = listOf(
        "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
        "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
        "seventeen", "eighteen", "nineteen",
    )

    private val tens = listOf(
        "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
    )

    /** Supported range: −999,999,999 … 999,999,999. Null outside it. */
    fun convert(value: Long): String? {
        if (value !in -999_999_999L..999_999_999L) return null
        if (value == 0L) return "zero"

        val negative = value < 0
        var remaining = if (negative) -value else value

        val millions = (remaining / 1_000_000).toInt()
        remaining %= 1_000_000
        val thousands = (remaining / 1_000).toInt()
        remaining %= 1_000
        val rest = remaining.toInt()

        val parts = buildList {
            if (millions > 0) add("${belowThousand(millions)} million")
            if (thousands > 0) add("${belowThousand(thousands)} thousand")
            if (rest > 0) add(belowThousand(rest))
        }

        val words = parts.joinToString(" ")
        return if (negative) "minus $words" else words
    }

    private fun belowThousand(n: Int): String {
        val hundreds = n / 100
        val rest = n % 100
        val parts = buildList {
            if (hundreds > 0) {
                add("${ones[hundreds]} hundred")
                if (rest > 0) add(belowHundred(rest))
            } else if (rest > 0) {
                add(belowHundred(rest))
            }
        }
        return parts.joinToString(" ")
    }

    private fun belowHundred(n: Int): String = when {
        n < 20 -> ones[n]
        n % 10 == 0 -> tens[n / 10]
        else -> "${tens[n / 10]}-${ones[n % 10]}"
    }
}
