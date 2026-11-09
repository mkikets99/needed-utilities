package com.github.mkikets99.neededutility.tools.calculator

import kotlin.math.abs
import kotlin.math.floor

/**
 * Immediate-execution calculator engine (like a standard phone calculator):
 * pressing an operator applies the pending operation right away.
 * Pure logic — covered by unit tests.
 */
class CalculatorEngine {

    var display: String = "0"
        private set

    /** Human-readable description of the last completed operation, e.g. "5 + 3". */
    var lastExpression: String? = null
        private set

    private var accumulator: Double? = null
    private var pendingOp: Char? = null
    private var entering: Boolean = false

    fun inputDigit(digit: Int) {
        require(digit in 0..9)
        if (!entering) {
            display = digit.toString()
            entering = true
        } else {
            display = if (display == "0") digit.toString()
            else if (display.length < 12) display + digit
            else display
        }
    }

    fun inputDot() {
        if (!entering) {
            display = "0."
            entering = true
        } else if ('.' !in display) {
            display += "."
        }
    }

    fun inputOp(op: Char) {
        require(op in charArrayOf('+', '-', '*', '/'))
        val current = display.toDoubleOrNull() ?: 0.0
        val acc = accumulator
        val result = if (acc != null && pendingOp != null && entering) {
            applyOp(acc, current, pendingOp!!)
        } else {
            current
        }
        accumulator = result
        display = format(result)
        pendingOp = op
        entering = false
    }

    fun equals() {
        val acc = accumulator ?: return
        val op = pendingOp ?: return
        val current = display.toDoubleOrNull() ?: 0.0
        val result = applyOp(acc, current, op)
        lastExpression = "${format(acc)} ${opSymbol(op)} ${format(current)}"
        display = format(result)
        accumulator = null
        pendingOp = null
        entering = false
    }

    fun clear() {
        display = "0"
        lastExpression = null
        accumulator = null
        pendingOp = null
        entering = false
    }

    fun toggleSign() {
        val current = display.toDoubleOrNull() ?: return
        display = format(-current)
    }

    fun percent() {
        val current = display.toDoubleOrNull() ?: return
        display = format(current / 100.0)
    }

    private fun applyOp(left: Double, right: Double, op: Char): Double = when (op) {
        '+' -> left + right
        '-' -> left - right
        '*' -> left * right
        '/' -> if (right == 0.0) Double.NaN else left / right
        else -> right
    }

    private fun opSymbol(op: Char): Char = when (op) {
        '*' -> '×'
        '/' -> '÷'
        '-' -> '−'
        else -> op
    }

    private fun format(value: Double): String = when {
        value.isNaN() -> "Error"
        value.isInfinite() -> "Error"
        value == floor(value) && abs(value) < 1e15 -> value.toLong().toString()
        else -> value.toString()
    }
}
