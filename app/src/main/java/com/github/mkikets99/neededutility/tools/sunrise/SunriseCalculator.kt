package com.github.mkikets99.neededutility.tools.sunrise

import java.time.Instant
import java.time.LocalDate
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

enum class PolarState { NORMAL, POLAR_DAY, POLAR_NIGHT }

data class SunriseSunsetResult(
    val sunriseUtc: Instant?,
    val sunsetUtc: Instant?,
    val solarNoonUtc: Instant,
    val polarState: PolarState,
)

/**
 * Sunrise / sunset via the standard sunrise equation (NOAA approximation).
 * All times are UTC; convert for display. Pure — covered by unit tests.
 */
object SunriseCalculator {

    /** Official zenith: -0.833° (sun radius + atmospheric refraction). */
    private const val ZENITH_DEG = -0.833

    fun calculate(date: LocalDate, latitude: Double, longitude: Double): SunriseSunsetResult? {
        if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) return null

        val julianDay = date.toEpochDay() + 2_440_587.5
        val n = ceil(julianDay - 2_451_545.0 + 0.0008)

        val meanSolarNoon = n - longitude / 360.0

        val meanAnomaly = normalize(357.5291 + 0.98560028 * meanSolarNoon)
        val mRad = Math.toRadians(meanAnomaly)

        val equationOfCenter =
            1.9148 * sin(mRad) + 0.02 * sin(2 * mRad) + 0.0003 * sin(3 * mRad)
        val eclipticLongitude = normalize(meanAnomaly + equationOfCenter + 180.0 + 102.9372)
        val lambdaRad = Math.toRadians(eclipticLongitude)

        val julianTransit =
            2_451_545.0 + meanSolarNoon + 0.0053 * sin(mRad) - 0.0069 * sin(2 * lambdaRad)

        val declination = asin(sin(lambdaRad) * sin(Math.toRadians(23.44)))
        val latitudeRad = Math.toRadians(latitude)
        val cosHourAngle =
            (sin(Math.toRadians(ZENITH_DEG)) - sin(latitudeRad) * sin(declination)) /
                (cos(latitudeRad) * cos(declination))

        val solarNoon = julianToInstant(julianTransit)

        return when {
            cosHourAngle > 1.0 -> SunriseSunsetResult(
                sunriseUtc = null,
                sunsetUtc = null,
                solarNoonUtc = solarNoon,
                polarState = PolarState.POLAR_NIGHT,
            )

            cosHourAngle < -1.0 -> SunriseSunsetResult(
                sunriseUtc = null,
                sunsetUtc = null,
                solarNoonUtc = solarNoon,
                polarState = PolarState.POLAR_DAY,
            )

            else -> {
                val hourAngle = Math.toDegrees(acos(cosHourAngle))
                SunriseSunsetResult(
                    sunriseUtc = julianToInstant(julianTransit - hourAngle / 360.0),
                    sunsetUtc = julianToInstant(julianTransit + hourAngle / 360.0),
                    solarNoonUtc = solarNoon,
                    polarState = PolarState.NORMAL,
                )
            }
        }
    }

    private fun normalize(degrees: Double): Double {
        val value = degrees % 360.0
        return if (value < 0) value + 360.0 else value
    }

    private fun julianToInstant(julianDay: Double): Instant =
        Instant.ofEpochMilli(((julianDay - 2_440_587.5) * 86_400_000.0).toLong())
}
