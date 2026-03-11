package com.example.kotlinmultisample.simple

import kotlinx.coroutines.delay

/**
 * 간단한 Repository (FruitRepository)
 * Data Layer: 데이터를 가져오는 로직 (여기서는 하드코딩된 리스트)
 *
 * * Interface 없음 (간소화)
 * * DataSource 분리 안 함 (간소화)
 */
class FruitRepository {
    private val fruits = listOf(
        Fruit(1, "Apple", "Red and sweet crisp apple", 1200),
        Fruit(2, "Banana", "Long yellow fruit, good for energy", 800),
        Fruit(3, "Orange", "Juicy citrus fruit, full of Vitamin C", 1500),
        Fruit(4, "Grape", "Small purple berries, sweet and sour", 3000),
        Fruit(5, "Mango", "Tropical sweet delight", 4500),
        Fruit(6, "Strawberry", "Red berries with seeds on the outside", 5000),
        Fruit(7, "Watermelon", "Big green fruit with red inside", 12000),
        Fruit(8, "Pineapple", "Spiky on the outside, sweet on the inside", 6000)
    )

    suspend fun getFruits(): List<Fruit> {
        // 네트워크 지연 시뮬레이션 (0.5초)
        delay(500)
        return fruits
    }
}

