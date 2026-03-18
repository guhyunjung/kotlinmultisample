package com.example.kotlinmultisample.app.util

/**
 * Minimal external declaration for JavaScript Date usable from Kotlin/JS (wasmJs).
 * This avoids direct js("...") calls and satisfies the compiler.
 */
external class JsDate {
    constructor()
    constructor(ms: Double)
    fun toISOString(): String

    companion object {
        fun now(): Double
    }
}


