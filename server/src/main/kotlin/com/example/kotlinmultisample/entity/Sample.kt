package com.example.kotlinmultisample.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "sample")
data class Sample(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	val username: String,
	val email: String,
	val name: String,
	val surname: String? = null,
	@JsonIgnore
	@OneToMany(mappedBy = "sample")
	val items: List<Address> = mutableListOf()
)