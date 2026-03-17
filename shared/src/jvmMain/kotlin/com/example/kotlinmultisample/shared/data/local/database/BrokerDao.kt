package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * 증권사 데이터 접근 객체 (DAO)
 * JVM Desktop 환경에서의 SQL 쿼리를 정의합니다.
 */
@Dao
interface BrokerDao {
    /**
     * 모든 증권사 목록 조회 (Reactive Flow)
     * 테이블 변경 시 자동으로 새로운 리스트를 방출합니다.
     */
    @Query("SELECT * FROM brokers")
    fun getAllBrokers(): Flow<List<BrokerEntity>>

    /**
     * 증권사 추가 또는 갱신
     * 충돌 시 대체(REPLACE) 전략 사용
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBroker(broker: BrokerEntity)

    /**
     * 이름으로 증권사 삭제
     */
    @Query("DELETE FROM brokers WHERE name = :name")
    suspend fun deleteBrokerByName(name: String)
}

