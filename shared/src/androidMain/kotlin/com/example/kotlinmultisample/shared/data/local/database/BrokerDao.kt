package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BrokerDao {
    @Query("SELECT * FROM brokers")
    fun getAllBrokers(): Flow<List<BrokerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBroker(broker: BrokerEntity)

    @Delete
    suspend fun deleteBroker(broker: BrokerEntity)
    
    @Query("DELETE FROM brokers WHERE name = :name")
    suspend fun deleteBrokerByName(name: String)
}

