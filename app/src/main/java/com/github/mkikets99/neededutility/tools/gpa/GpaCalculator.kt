package com.github.mkikets99.neededutility.tools.gpa

/**
 * Grade point average on the US 4.0 scale. Pure — covered by unit tests.
 */
object GpaCalculator {

    val GRADE_POINTS: Map<String, Double> = linkedMapOf(
        "A+" to 4.0, "A" to 4.0, "A-" to 3.7,
        "B+" to 3.3, "B" to 3.0, "B-" to 2.7,
        "C+" to 2.3, "C" to 2.0, "C-" to 1.7,
        "D+" to 1.3, "D" to 1.0, "D-" to 0.7,
        "F" to 0.0,
    )

    /**
     * Weighted GPA over (grade, credit) pairs. Null for invalid input.
     */
    fun calculate(courses: List<Pair<String, Double>>): Double? {
        if (courses.isEmpty()) return null
        var totalPoints = 0.0
        var totalCredits = 0.0
        for ((grade, credits) in courses) {
            if (credits <= 0.0) return null
            val points = GRADE_POINTS[grade.trim().uppercase()] ?: return null
            totalPoints += points * credits
            totalCredits += credits
        }
        return if (totalCredits == 0.0) null else totalPoints / totalCredits
    }
}
