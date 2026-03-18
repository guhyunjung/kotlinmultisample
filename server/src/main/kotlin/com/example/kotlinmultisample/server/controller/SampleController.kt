package com.example.kotlinmultisample.server.controller

import com.example.kotlinmultisample.server.service.SampleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController(getSampleService: SampleService) {

	@GetMapping("/")
	fun greet(): String {
		return ""
	}

	@GetMapping("/user")
	fun sample(): String {

		return ""
	}
}