package com.github.mkikets99.neededutility.tools.speed

data class SpeedResult(
    val speed: Double,
    val distance: Double,
    val timeHours: Double,
)

/**
 * Speed / distance / time solver: give any two values, get the third.
 * Pure — covered by unit tests.
 */
object SpeedTimeDistance {

    fun solve(distance: Double?, timeHours: Double?, speed: Double?): SpeedResult? {
        val filled = listOfNotNull(distance, timeHours, speed).size
        if (filled != 2) return null
        listOfNotNull(distance, timeHours, speed).forEach { if (it <= 0.0) return null }

        return when {
            speed == null -> {
                val d = distance!!
                val t = timeHours!!
                SpeedResult(speed = d / t, distance = d, timeHours = t)
            }

            distance == null -> {
                val s = speed
                val t = timeHours!!
                SpeedResult(speed = s, distance = s * t, timeHours = t)
            }

            else -> {
                val s = speed
                val d = distance
                SpeedResult(speed = s, distance = d, timeHours = d / s)
            }
        }
    }
}
