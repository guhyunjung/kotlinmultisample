package com.example.kotlinmultisample.server.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "address")
data class Address(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sample_id")
	val sample: Sample? = null,
	val title: String,
	val country: String,
	val city: String,
	val town: String? = null,
	val detail: String
)