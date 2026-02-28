package com.example.kotlinmultisample.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "address")
data class Address(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sample_id")
	val sample: Sample,
	val title: String,
	val country: String,
	val city: String,
	val town: String? = null,
	val detail: String
)