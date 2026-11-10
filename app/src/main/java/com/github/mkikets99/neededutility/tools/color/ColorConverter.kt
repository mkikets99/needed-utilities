package com.github.mkikets99.neededutility.tools.color

data class RgbColor(val red: Int, val green: Int, val blue: Int) {
    val hex: String
        get() = "#%02X%02X%02X".format(red, green, blue)
}

/**
 * Hex ↔ RGB color conversion. Pure — covered by unit tests.
 */
object ColorConverter {

    /** Accepts "#RRGGBB" or "RRGGBB" (case-insensitive). */
    fun fromHex(hex: String): RgbColor? {
        val cleaned = hex.trim().removePrefix("#")
        if (cleaned.length != 6 || !cleaned.all { it.isHexDigit() }) return null
        return RgbColor(
            red = cleaned.substring(0, 2).toInt(16),
            green = cleaned.substring(2, 4).toInt(16),
            blue = cleaned.substring(4, 6).toInt(16),
        )
    }

    fun toHex(red: Int, green: Int, blue: Int): String =
        RgbColor(red.coerceIn(0, 255), green.coerceIn(0, 255), blue.coerceIn(0, 255)).hex

    private fun Char.isHexDigit(): Boolean =
        this in '0'..'9' || this in 'a'..'f' || this in 'A'..'F'
}
