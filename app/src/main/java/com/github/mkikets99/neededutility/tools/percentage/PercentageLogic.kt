package com.github.mkikets99.neededutility.tools.percentage

/**
 * Percentage math. Pure — covered by unit tests.
 */
object PercentageLogic {

    /** "What is P% of X?" */
    fun percentOf(percent: Double, value: Double): Double = percent / 100.0 * value

    /** "X is what percent of Y?" Returns null when Y is zero. */
    fun whatPercentIs(part: Double, whole: Double): Double? =
        if (whole == 0.0) null else part / whole * 100.0

    /** "Percent change from X to Y." Returns null when X is zero. */
    fun percentChange(from: Double, to: Double): Double? =
        if (from == 0.0) null else (to - from) / from * 100.0

    /** "X plus P%." */
    fun addPercent(value: Double, percent: Double): Double = value + percentOf(percent, value)
}
