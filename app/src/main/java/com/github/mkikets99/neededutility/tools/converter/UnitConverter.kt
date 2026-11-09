package com.github.mkikets99.neededutility.tools.converter

/**
 * A single unit of measurement. [factorToBase] converts one unit into the
 * base unit of its category (ignored for temperature, which is affine).
 */
data class ConvertUnit(val name: String, val symbol: String, val factorToBase: Double)

/**
 * Conversion categories shipped in v1. New categories are a new enum entry.
 */
enum class ConvertCategory(val title: String, val units: List<ConvertUnit>) {
    LENGTH(
        "Length", listOf(
            ConvertUnit("Millimeter", "mm", 0.001),
            ConvertUnit("Centimeter", "cm", 0.01),
            ConvertUnit("Meter", "m", 1.0),
            ConvertUnit("Kilometer", "km", 1000.0),
            ConvertUnit("Inch", "in", 0.0254),
            ConvertUnit("Foot", "ft", 0.3048),
            ConvertUnit("Yard", "yd", 0.9144),
            ConvertUnit("Mile", "mi", 1609.344),
        )
    ),
    MASS(
        "Mass", listOf(
            ConvertUnit("Milligram", "mg", 1e-6),
            ConvertUnit("Gram", "g", 0.001),
            ConvertUnit("Kilogram", "kg", 1.0),
            ConvertUnit("Tonne", "t", 1000.0),
            ConvertUnit("Ounce", "oz", 0.028349523125),
            ConvertUnit("Pound", "lb", 0.45359237),
        )
    ),
    TEMPERATURE(
        "Temperature", listOf(
            ConvertUnit("Celsius", "C", 1.0),
            ConvertUnit("Fahrenheit", "F", 1.0),
            ConvertUnit("Kelvin", "K", 1.0),
        )
    ),
    VOLUME(
        "Volume", listOf(
            ConvertUnit("Milliliter", "ml", 0.001),
            ConvertUnit("Liter", "l", 1.0),
            ConvertUnit("Teaspoon (US)", "tsp", 0.00492892159375),
            ConvertUnit("Tablespoon (US)", "tbsp", 0.01478676478125),
            ConvertUnit("Fluid ounce (US)", "fl oz", 0.0295735295625),
            ConvertUnit("Cup (US)", "cup", 0.2365882365),
            ConvertUnit("Gallon (US)", "gal", 3.785411784),
        )
    ),
}

/**
 * Unit conversion logic. Pure — covered by unit tests.
 */
object UnitConverter {

    fun convert(category: ConvertCategory, fromSymbol: String, toSymbol: String, value: Double): Double {
        if (fromSymbol == toSymbol) return value
        return when (category) {
            ConvertCategory.TEMPERATURE -> convertTemperature(fromSymbol, toSymbol, value)
            else -> {
                val from = category.units.first { it.symbol == fromSymbol }
                val to = category.units.first { it.symbol == toSymbol }
                value * from.factorToBase / to.factorToBase
            }
        }
    }

    private fun convertTemperature(fromSymbol: String, toSymbol: String, value: Double): Double {
        val celsius = when (fromSymbol) {
            "C" -> value
            "F" -> (value - 32.0) * 5.0 / 9.0
            "K" -> value - 273.15
            else -> value
        }
        return when (toSymbol) {
            "C" -> celsius
            "F" -> celsius * 9.0 / 5.0 + 32.0
            "K" -> celsius + 273.15
            else -> celsius
        }
    }
}
