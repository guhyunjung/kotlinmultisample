package com.example.kotlinmultisample.controller

import com.example.kotlinmultisample.Greeting
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {

	@GetMapping("/")
	fun greet(): String {
		return "Spring Boot: ${Greeting().greet()}"
	}
}