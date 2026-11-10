package com.github.mkikets99.neededutility.tools.roman

/**
 * Roman numeral conversion (1..3999). Pure — covered by unit tests.
 */
object RomanNumerals {

    private val values = listOf(
        1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
        100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
        10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I",
    )

    fun toRoman(value: Int): String? {
        if (value !in 1..3999) return null
        var remaining = value
        val result = StringBuilder()
        for ((amount, numeral) in values) {
            while (remaining >= amount) {
                result.append(numeral)
                remaining -= amount
            }
        }
        return result.toString()
    }

    fun fromRoman(roman: String): Int? {
        val text = roman.trim().uppercase()
        if (text.isEmpty() || !text.all { it in "IVXLCDM" }) return null
        var total = 0
        var index = 0
        while (index < text.length) {
            val current = valueOf(text[index])
            val next = if (index + 1 < text.length) valueOf(text[index + 1]) else 0
            if (current < next) {
                total += next - current
                index += 2
            } else {
                total += current
                index += 1
            }
        }
        // Reject malformed numerals like "IIII" by round-tripping.
        return if (toRoman(total) == text) total else null
    }

    private fun valueOf(ch: Char): Int = when (ch) {
        'I' -> 1
        'V' -> 5
        'X' -> 10
        'L' -> 50
        'C' -> 100
        'D' -> 500
        'M' -> 1000
        else -> 0
    }
}
