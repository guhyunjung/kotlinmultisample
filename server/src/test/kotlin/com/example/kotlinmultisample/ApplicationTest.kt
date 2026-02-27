package com.example.kotlinmultisample

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.junit.jupiter.api.Assertions.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

	@LocalServerPort
	private var port: Int = 0

	@Autowired
	private lateinit var restTemplate: TestRestTemplate

	@Test
	fun testRoot() {
		val response = restTemplate.getForEntity("http://localhost:$port/", String::class.java)
		assertEquals(200, response.statusCode.value())
		assertEquals("Spring Boot: ${Greeting().greet()}", response.body)
	}
}