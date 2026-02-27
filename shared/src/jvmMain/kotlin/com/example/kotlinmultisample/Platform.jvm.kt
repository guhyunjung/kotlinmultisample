package com.example.kotlinmultisample

class JVMPlatform : Platform {
	override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()