package com.example.kotlinmultisample.shared.data.local.datasource

import com.example.kotlinmultisample.shared.domain.model.Broker
import kotlinx.coroutines.flow.Flow

/**
 * 로컬 데이터 소스 인터페이스 (농장 기능)
 * 플랫폼별 구현체(Android/JVM)가 이 인터페이스를 구현하여 Room DB에 접근합니다.
 */
interface LocalFarmDataSource {
    /**
     * 저장된 증권사 목록 조회
     */
    fun getBrokers(): Flow<List<Broker>>

    /**
     * 증권사 추가
     */
    suspend fun insertBroker(broker: Broker)

    /**
     * 이름으로 증권사 삭제
     */
    suspend fun deleteBrokerByName(name: String)
}
