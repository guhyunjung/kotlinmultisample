package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinmultisample.shared.domain.model.Broker

/**
 * 증권사 Room Entity (Android)
 *
 * 로컬 데이터베이스의 'brokers' 테이블과 매핑됩니다.
 * Android 환경에서 Room이 이 클래스를 사용하여 테이블 스키마를 생성합니다.
 */
@Entity(tableName = "brokers")
data class BrokerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

/**
 * Entity -> Domain 변환 확장 함수
 */
fun BrokerEntity.toDomain(): Broker {
    return Broker(
        id = id,
        name = name
    )
}

/**
 * Domain -> Entity 변환 확장 함수
 */
fun Broker.toEntity(): BrokerEntity {
    return BrokerEntity(
        id = id,
        name = name
    )
}
