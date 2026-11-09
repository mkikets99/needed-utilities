package com.github.mkikets99.neededutility.tools.bases

/**
 * Number base conversion (binary / octal / decimal / hexadecimal).
 * Pure — covered by unit tests.
 */
object BaseConverter {

    val SUPPORTED_RADICES = listOf(2, 8, 10, 16)

    /** Parses [value] in the given [radix]; null when invalid or negative. */
    fun parse(value: String, radix: Int): Long? {
        val cleaned = value.trim()
        if (cleaned.isEmpty()) return null
        return cleaned.toLongOrNull(radix)?.takeIf { it >= 0 }
    }

    /** Formats [value] in the given [radix] (hex is uppercase). */
    fun format(value: Long, radix: Int): String = when (radix) {
        16 -> value.toString(16).uppercase()
        else -> value.toString(radix)
    }
}
