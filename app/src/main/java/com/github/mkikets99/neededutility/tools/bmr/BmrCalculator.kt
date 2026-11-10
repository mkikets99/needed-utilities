package com.github.mkikets99.neededutility.tools.bmr

enum class BiologicalSex { MALE, FEMALE }

data class BmrResult(
    /** Basal metabolic rate, kcal/day. */
    val bmr: Double,
    /** BMR × activity multiplier, kcal/day. */
    val maintenance: Double,
)

/**
 * Mifflin-St Jeor basal metabolic rate. Pure — covered by unit tests.
 */
object BmrCalculator {

    val ACTIVITY_LEVELS = listOf(
        "Sedentary" to 1.2,
        "Light" to 1.375,
        "Moderate" to 1.55,
        "Active" to 1.725,
        "Very active" to 1.9,
    )

    fun calculate(
        sex: BiologicalSex,
        ageYears: Int,
        heightCm: Double,
        weightKg: Double,
        activityMultiplier: Double,
    ): BmrResult? {
        if (ageYears <= 0 || heightCm <= 0.0 || weightKg <= 0.0) return null
        if (activityMultiplier <= 0.0) return null
        val sexOffset = when (sex) {
            BiologicalSex.MALE -> 5.0
            BiologicalSex.FEMALE -> -161.0
        }
        val bmr = 10.0 * weightKg + 6.25 * heightCm - 5.0 * ageYears + sexOffset
        return BmrResult(bmr = bmr, maintenance = bmr * activityMultiplier)
    }
}
