package com.github.mkikets99.neededutility.tools.loan

import kotlin.math.pow

data class LoanResult(
    val monthlyPayment: Double,
    val totalPayment: Double,
    val totalInterest: Double,
)

/**
 * Loan / EMI math (fixed-rate amortizing loan). Pure — covered by unit tests.
 */
object LoanCalculator {

    /**
     * Standard EMI formula: P * r * (1+r)^n / ((1+r)^n - 1), r = monthly rate.
     * Zero interest loans use P / n. Returns null for non-positive inputs.
     */
    fun calculate(principal: Double, annualRatePercent: Double, months: Int): LoanResult? {
        if (principal <= 0.0 || months <= 0 || annualRatePercent < 0.0) return null
        val monthlyRate = annualRatePercent / 100.0 / 12.0
        val emi = if (monthlyRate == 0.0) {
            principal / months
        } else {
            val growth = (1.0 + monthlyRate).pow(months)
            principal * monthlyRate * growth / (growth - 1.0)
        }
        val total = emi * months
        return LoanResult(
            monthlyPayment = emi,
            totalPayment = total,
            totalInterest = total - principal,
        )
    }
}
