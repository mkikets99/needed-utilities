package com.github.mkikets99.neededutility.tools.randomizer

import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordStrengthTest {

    @Test
    fun emptyIsWeak() {
        assertEquals(PasswordStrength.WEAK, PasswordStrengthEvaluator.evaluate(""))
    }

    @Test
    fun shortSimplePasswordIsWeak() {
        assertEquals(PasswordStrength.WEAK, PasswordStrengthEvaluator.evaluate("abc"))
    }

    @Test
    fun simpleLettersOnlyPasswordIsWeak() {
        // 8 chars but no case mix, digits or symbols → weak
        assertEquals(PasswordStrength.WEAK, PasswordStrengthEvaluator.evaluate("abcdefgh"))
    }

    @Test
    fun mixedShortPasswordIsFair() {
        assertEquals(PasswordStrength.FAIR, PasswordStrengthEvaluator.evaluate("Abcdefg1"))
    }

    @Test
    fun longMixedPasswordIsStrong() {
        assertEquals(PasswordStrength.STRONG, PasswordStrengthEvaluator.evaluate("aB3\$aB3\$aB3\$aB3\$"))
    }

    @Test
    fun lengthAndVarietyIncreaseScore() {
        val weak = PasswordStrengthEvaluator.evaluate("aaaaaaa")
        val stronger = PasswordStrengthEvaluator.evaluate("Aa1!aaaa")
        assert(weak.ordinal <= stronger.ordinal)
    }
}
