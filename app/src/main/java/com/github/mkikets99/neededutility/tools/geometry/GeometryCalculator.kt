package com.github.mkikets99.neededutility.tools.geometry

import kotlin.math.sqrt

/**
 * Area and perimeter for circles, rectangles and triangles.
 * Pure — covered by unit tests.
 */
object GeometryCalculator {

    fun circleArea(radius: Double): Double? {
        if (radius <= 0.0) return null
        return Math.PI * radius * radius
    }

    fun circleCircumference(radius: Double): Double? {
        if (radius <= 0.0) return null
        return 2.0 * Math.PI * radius
    }

    fun rectangleArea(width: Double, height: Double): Double? {
        if (width <= 0.0 || height <= 0.0) return null
        return width * height
    }

    fun rectanglePerimeter(width: Double, height: Double): Double? {
        if (width <= 0.0 || height <= 0.0) return null
        return 2.0 * (width + height)
    }

    /** Heron's formula; returns null for invalid triangle sides. */
    fun triangleArea(a: Double, b: Double, c: Double): Double? {
        if (a <= 0.0 || b <= 0.0 || c <= 0.0) return null
        if (a + b <= c || a + c <= b || b + c <= a) return null
        val s = (a + b + c) / 2.0
        return sqrt(s * (s - a) * (s - b) * (s - c))
    }

    fun trianglePerimeter(a: Double, b: Double, c: Double): Double? {
        if (a <= 0.0 || b <= 0.0 || c <= 0.0) return null
        if (a + b <= c || a + c <= b || b + c <= a) return null
        return a + b + c
    }
}
