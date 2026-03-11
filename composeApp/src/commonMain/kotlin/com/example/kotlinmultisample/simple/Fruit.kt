package com.example.kotlinmultisample.simple

/**
 * 간단한 데이터 모델 (Fruit)
 * Data Layer: 순수 데이터 클래스
 */
data class Fruit(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int
)

