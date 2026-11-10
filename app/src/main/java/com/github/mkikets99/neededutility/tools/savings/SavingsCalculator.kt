package com.github.mkikets99.neededutility.tools.savings

import kotlin.math.pow

data class SavingsResult(
    val futureValue: Double,
    val totalContributed: Double,
    val interestEarned: Double,
)

/**
 * Compound interest with optional monthly deposits (compounded monthly).
 * Pure — covered by unit tests.
 */
object SavingsCalculator {

    fun calculate(
        principal: Double,
        annualRatePercent: Double,
        years: Int,
        monthlyDeposit: Double,
    ): SavingsResult? {
        if (principal < 0.0 || monthlyDeposit < 0.0) return null
        if (years <= 0 || annualRatePercent < 0.0) return null
        if (principal == 0.0 && monthlyDeposit == 0.0) return null

        val months = years * 12
        val monthlyRate = annualRatePercent / 100.0 / 12.0

        val principalFuture = principal * (1.0 + monthlyRate).pow(months)
        val depositsFuture = if (monthlyRate == 0.0) {
            monthlyDeposit * months
        } else {
            monthlyDeposit * ((1.0 + monthlyRate).pow(months) - 1.0) / monthlyRate
        }

        val futureValue = principalFuture + depositsFuture
        val contributed = principal + monthlyDeposit * months
        return SavingsResult(
            futureValue = futureValue,
            totalContributed = contributed,
            interestEarned = futureValue - contributed,
        )
    }
}
