package com.example.kotlinmultisample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.kotlinmultisample.shared.di.initKoin

fun main() = application {
	initKoin()

	Window(
		onCloseRequest = ::exitApplication,
		title = "kotlinmultisample",
	) {
		App()
	}
}