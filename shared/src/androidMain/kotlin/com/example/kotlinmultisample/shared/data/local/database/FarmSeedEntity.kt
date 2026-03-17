package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.kotlinmultisample.shared.domain.model.FarmSeed

/**
 * 농작물(주식 종목) Room Entity (Android)
 *
 * 'seeds' 테이블과 매핑됩니다.
 * 'brokers' 테이블과 외래키 관계(Foreign Key)를 가집니다.
 * 증권사(Broker)가 삭제되면 해당 증권사의 농작물도 함께 삭제(CASCADE)됩니다.
 */
@Entity(
    tableName = "seeds",
    foreignKeys = [
        ForeignKey(
            entity = BrokerEntity::class,
            parentColumns = ["id"],
            childColumns = ["brokerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FarmSeedEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val brokerId: Long,
    val name: String,
    val emoji: String,
    val pct: Double,
    val buyingPrice: Double,
    val quantity: Int
)

/**
 * Entity -> Domain 변환 확장 함수
 */
fun FarmSeedEntity.toDomain(): FarmSeed {
    return FarmSeed(
        id = id,
        brokerId = brokerId,
        name = name,
        emoji = emoji,
        pct = pct,
        buyingPrice = buyingPrice,
        quantity = quantity
    )
}

/**
 * Domain -> Entity 변환 확장 함수
 */
fun FarmSeed.toEntity(): FarmSeedEntity {
    return FarmSeedEntity(
        id = id,
        brokerId = brokerId,
        name = name,
        emoji = emoji,
        pct = pct,
        buyingPrice = buyingPrice,
        quantity = quantity
    )
}

