package com.github.mkikets99.neededutility.tools.geometry

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.math.PI

class GeometryCalculatorTest {

    private val delta = 1e-6

    @Test
    fun circle() {
        assertEquals(PI * 25.0, GeometryCalculator.circleArea(5.0)!!, delta)
        assertEquals(10.0 * PI, GeometryCalculator.circleCircumference(5.0)!!, delta)
    }

    @Test
    fun rectangle() {
        assertEquals(12.0, GeometryCalculator.rectangleArea(3.0, 4.0)!!, delta)
        assertEquals(14.0, GeometryCalculator.rectanglePerimeter(3.0, 4.0)!!, delta)
    }

    @Test
    fun rightTriangle() {
        // 3-4-5 triangle: area 6, perimeter 12
        assertEquals(6.0, GeometryCalculator.triangleArea(3.0, 4.0, 5.0)!!, delta)
        assertEquals(12.0, GeometryCalculator.trianglePerimeter(3.0, 4.0, 5.0)!!, delta)
    }

    @Test
    fun invalidTriangleRejected() {
        assertNull(GeometryCalculator.triangleArea(1.0, 2.0, 10.0))
        assertNull(GeometryCalculator.trianglePerimeter(1.0, 1.0, 2.0))
    }

    @Test
    fun nonPositiveDimensionsRejected() {
        assertNull(GeometryCalculator.circleArea(0.0))
        assertNull(GeometryCalculator.rectangleArea(-1.0, 5.0))
        assertNull(GeometryCalculator.triangleArea(3.0, 0.0, 5.0))
    }
}
