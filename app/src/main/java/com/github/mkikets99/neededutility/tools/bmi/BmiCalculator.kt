package com.github.mkikets99.neededutility.tools.bmi

data class BmiResult(
    val bmi: Double,
    val category: String,
    val healthyWeightFrom: Double,
    val healthyWeightTo: Double,
)

/**
 * Body mass index (metric). Pure — covered by unit tests.
 */
object BmiCalculator {

    fun calculate(heightCm: Double, weightKg: Double): BmiResult? {
        if (heightCm <= 0.0 || weightKg <= 0.0) return null
        val meters = heightCm / 100.0
        val bmi = weightKg / (meters * meters)
        val category = when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal weight"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
        return BmiResult(
            bmi = bmi,
            category = category,
            healthyWeightFrom = 18.5 * meters * meters,
            healthyWeightTo = 24.9 * meters * meters,
        )
    }
}
