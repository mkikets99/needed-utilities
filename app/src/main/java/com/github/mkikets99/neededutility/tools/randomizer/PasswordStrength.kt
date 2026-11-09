package com.github.mkikets99.neededutility.tools.randomizer

/**
 * Rough password strength estimate for the password generator.
 * Pure — covered by unit tests.
 */
enum class PasswordStrength(val label: String) {
    WEAK("Weak"),
    FAIR("Fair"),
    GOOD("Good"),
    STRONG("Strong"),
}

object PasswordStrengthEvaluator {

    fun evaluate(password: String): PasswordStrength {
        if (password.isEmpty()) return PasswordStrength.WEAK
        var score = 0
        if (password.length >= 8) score++
        if (password.length >= 12) score++
        if (password.length >= 16) score++
        if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++
        return when {
            score <= 1 -> PasswordStrength.WEAK
            score <= 3 -> PasswordStrength.FAIR
            score <= 4 -> PasswordStrength.GOOD
            else -> PasswordStrength.STRONG
        }
    }
}
