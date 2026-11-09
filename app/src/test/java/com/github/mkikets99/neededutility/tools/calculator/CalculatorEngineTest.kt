package com.github.mkikets99.neededutility.tools.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorEngineTest {

    @Test
    fun addition() {
        val engine = CalculatorEngine()
        engine.inputDigit(1)
        engine.inputOp('+')
        engine.inputDigit(2)
        engine.equals()
        assertEquals("3", engine.display)
    }

    @Test
    fun immediateExecutionAppliesPendingOperationFirst() {
        // 5 + 3 * 2 = 16 under immediate execution: (5+3) then *2
        val engine = CalculatorEngine()
        engine.inputDigit(5)
        engine.inputOp('+')
        engine.inputDigit(3)
        engine.inputOp('*')
        engine.inputDigit(2)
        engine.equals()
        assertEquals("16", engine.display)
    }

    @Test
    fun division() {
        val engine = CalculatorEngine()
        engine.inputDigit(1)
        engine.inputDigit(2)
        engine.inputOp('/')
        engine.inputDigit(4)
        engine.equals()
        assertEquals("3", engine.display)
    }

    @Test
    fun divisionByZeroShowsError() {
        val engine = CalculatorEngine()
        engine.inputDigit(5)
        engine.inputOp('/')
        engine.inputDigit(0)
        engine.equals()
        assertEquals("Error", engine.display)
    }

    @Test
    fun percentConvertsToFraction() {
        val engine = CalculatorEngine()
        engine.inputDigit(5)
        engine.inputDigit(0)
        engine.percent()
        assertEquals("0.5", engine.display)
    }

    @Test
    fun clearResetsEverything() {
        val engine = CalculatorEngine()
        engine.inputDigit(9)
        engine.inputOp('+')
        engine.clear()
        assertEquals("0", engine.display)
        engine.inputDigit(4)
        engine.equals()
        assertEquals("4", engine.display)
    }

    @Test
    fun toggleSign() {
        val engine = CalculatorEngine()
        engine.inputDigit(7)
        engine.toggleSign()
        assertEquals("-7", engine.display)
    }
}
