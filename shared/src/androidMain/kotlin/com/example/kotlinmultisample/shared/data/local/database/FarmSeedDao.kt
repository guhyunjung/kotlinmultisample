package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 농작물(주식 종목) 데이터 접근 객체 (DAO)
 */
@Dao
interface FarmSeedDao {
    /**
     * 모든 농작물 목록 조회
     */
    @Query("SELECT * FROM seeds")
    fun getAllSeeds(): Flow<List<FarmSeedEntity>>

    /**
     * 특정 증권사의 농작물 목록 조회
     */
    @Query("SELECT * FROM seeds WHERE brokerId = :brokerId")
    fun getSeedsByBroker(brokerId: Long): Flow<List<FarmSeedEntity>>

    /**
     * 농작물 추가
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeed(seed: FarmSeedEntity)

    /**
     * 농작물 수정
     */
    @Update
    suspend fun updateSeed(seed: FarmSeedEntity)

    /**
     * 농작물 삭제
     */
    @Delete
    suspend fun deleteSeed(seed: FarmSeedEntity)
}

