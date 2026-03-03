package com.example.kotlinmultisample.server.repository

import com.example.kotlinmultisample.server.entity.Sample
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SampleRepository : JpaRepository<Sample, Long> {

	fun getSampleById(id: Long): Sample?

	override fun findAll(): List<Sample>

}