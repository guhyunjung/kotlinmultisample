package com.example.kotlinmultisample

interface Platform {
	val name: String
}

expect fun getPlatform(): Platform