package com.github.mkikets99.neededutility.tools.converter

import org.junit.Assert.assertEquals
import org.junit.Test

class UnitConverterTest {

    private val delta = 1e-9

    @Test
    fun lengthKilometerToMeter() {
        val result = UnitConverter.convert(ConvertCategory.LENGTH, "km", "m", 1.0)
        assertEquals(1000.0, result, delta)
    }

    @Test
    fun lengthMileToKilometer() {
        val result = UnitConverter.convert(ConvertCategory.LENGTH, "mi", "km", 1.0)
        assertEquals(1.609344, result, delta)
    }

    @Test
    fun massPoundToKilogram() {
        val result = UnitConverter.convert(ConvertCategory.MASS, "lb", "kg", 1.0)
        assertEquals(0.45359237, result, delta)
    }

    @Test
    fun temperatureCelsiusToFahrenheit() {
        val result = UnitConverter.convert(ConvertCategory.TEMPERATURE, "C", "F", 0.0)
        assertEquals(32.0, result, delta)
    }

    @Test
    fun temperatureCelsiusToKelvin() {
        val result = UnitConverter.convert(ConvertCategory.TEMPERATURE, "C", "K", 100.0)
        assertEquals(373.15, result, delta)
    }

    @Test
    fun temperatureFahrenheitToCelsius() {
        val result = UnitConverter.convert(ConvertCategory.TEMPERATURE, "F", "C", 212.0)
        assertEquals(100.0, result, delta)
    }

    @Test
    fun sameUnitIsIdentity() {
        val result = UnitConverter.convert(ConvertCategory.VOLUME, "l", "l", 3.5)
        assertEquals(3.5, result, delta)
    }

    @Test
    fun volumeGallonToLiter() {
        val result = UnitConverter.convert(ConvertCategory.VOLUME, "gal", "l", 1.0)
        assertEquals(3.785411784, result, delta)
    }
}
