package com.example.kotlinmultisample.shared.data.local.datasource

import com.example.kotlinmultisample.shared.domain.model.Broker
import com.example.kotlinmultisample.shared.domain.model.Diary
import com.example.kotlinmultisample.shared.domain.model.FarmSeed
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

    /**
     * 증권사 정보 수정
     */
    suspend fun updateBroker(broker: Broker)
    
    // --- Seed 관련 메서드 추가 ---
    
    fun getSeeds(): Flow<List<FarmSeed>>
    
    fun getSeedsByBroker(brokerId: Long): Flow<List<FarmSeed>>
    
    suspend fun insertSeed(seed: FarmSeed)
    
    suspend fun updateSeed(seed: FarmSeed)
    
    suspend fun deleteSeed(seed: FarmSeed)

    // --- Diary 관련 메서드 추가 ---

    fun getDiaries(): Flow<List<Diary>>

    suspend fun insertDiary(entry: Diary)

    suspend fun updateDiary(entry: Diary)

    suspend fun deleteDiary(id: Long)
}
