package com.example.kotlinmultisample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class KotlinMultiSampleApplication

fun main(args: Array<String>) {
	runApplication<KotlinMultiSampleApplication>(*args)
}

@RestController
class GreetingController {

	@GetMapping("/")
	fun greet(): String {
		return "Spring Boot: ${Greeting().greet()}"
	}
}