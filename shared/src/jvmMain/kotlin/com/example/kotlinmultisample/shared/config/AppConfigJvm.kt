package com.example.kotlinmultisample.shared.config

actual fun loadResourceText(path: String): String? {
    return object {}.javaClass.getResource(path)?.readText()
}

