package com.example.kotlinmultisample

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class Calculator {
	fun add(a: Int, b: Int): Int = a + b
	fun subtract(a: Int, b: Int): Int = a - b
	fun multiply(a: Int, b: Int): Int = a * b
	fun divide(a: Double, b: Double): Double = if (b != 0.0) a / b else 0.0
}