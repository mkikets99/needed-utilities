package com.github.mkikets99.neededutility.tools.sunrise

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class SunriseCalculatorTest {

    @Test
    fun equatorEquinox() {
        // At lat/lon 0 on the equinox: sunrise ≈ 06:00 UTC, sunset ≈ 18:00 UTC,
        // solar noon ≈ 12:00 UTC (equation of time allows a few minutes of slack).
        val date = LocalDate.of(2026, 3, 20)
        val result = SunriseCalculator.calculate(date, 0.0, 0.0)!!

        assertEquals(PolarState.NORMAL, result.polarState)
        val sunrise = result.sunriseUtc!!
        val sunset = result.sunsetUtc!!

        val sunriseExpected = date.atTime(6, 0).toInstant(ZoneOffset.UTC)
        val sunsetExpected = date.atTime(18, 0).toInstant(ZoneOffset.UTC)
        val noonExpected = date.atTime(12, 0).toInstant(ZoneOffset.UTC)

        assert(kotlin.math.abs(ChronoUnit.MINUTES.between(sunriseExpected, sunrise)) <= 10)
        assert(kotlin.math.abs(ChronoUnit.MINUTES.between(sunsetExpected, sunset)) <= 10)
        assert(kotlin.math.abs(ChronoUnit.MINUTES.between(noonExpected, result.solarNoonUtc)) <= 10)
    }

    @Test
    fun summerPolarDay() {
        val result = SunriseCalculator.calculate(LocalDate.of(2026, 6, 21), 85.0, 0.0)!!
        assertEquals(PolarState.POLAR_DAY, result.polarState)
        assertNull(result.sunriseUtc)
        assertNull(result.sunsetUtc)
    }

    @Test
    fun winterPolarNight() {
        val result = SunriseCalculator.calculate(LocalDate.of(2026, 12, 21), 85.0, 0.0)!!
        assertEquals(PolarState.POLAR_NIGHT, result.polarState)
    }

    @Test
    fun highLatitudeSummerHasLongDay() {
        // Kyiv (50.45, 30.52) in June: day length > 15 hours.
        val result = SunriseCalculator.calculate(LocalDate.of(2026, 6, 21), 50.45, 30.52)!!
        assertEquals(PolarState.NORMAL, result.polarState)
        val dayLength = ChronoUnit.MINUTES.between(result.sunriseUtc, result.sunsetUtc)
        assert(dayLength > 15 * 60)
    }

    @Test
    fun invalidCoordinates() {
        assertNull(SunriseCalculator.calculate(LocalDate.of(2026, 1, 1), 91.0, 0.0))
        assertNull(SunriseCalculator.calculate(LocalDate.of(2026, 1, 1), 0.0, 181.0))
    }

    @Test
    fun solarNoonAlwaysPresent() {
        val result = SunriseCalculator.calculate(LocalDate.of(2026, 1, 1), 60.0, 25.0)
        assertNotNull(result!!.solarNoonUtc)
    }
}
