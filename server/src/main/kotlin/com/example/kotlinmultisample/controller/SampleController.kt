package com.example.kotlinmultisample.controller

import com.example.kotlinmultisample.Greeting
import com.example.kotlinmultisample.service.SampleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController(getSampleService: SampleService) {

	@GetMapping("/")
	fun greet(): String {
		return "Spring Boot: ${Greeting().greet()}"
	}

	@GetMapping("/user")
	fun sample(): String {

		return ""
	}
}