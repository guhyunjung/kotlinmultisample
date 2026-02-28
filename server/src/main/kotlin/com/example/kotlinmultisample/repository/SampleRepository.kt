package com.example.kotlinmultisample.repository

import com.example.kotlinmultisample.entity.Sample
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SampleRepository : JpaRepository<Sample, Long> {

	fun getSampleById(id: Long): Sample?

	override fun findAll(): List<Sample>

}