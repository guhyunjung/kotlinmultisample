package com.example.kotlinmultisample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
	Window(
		onCloseRequest = ::exitApplication,
		title = "kotlinmultisample",
	) {
		App()
	}
}