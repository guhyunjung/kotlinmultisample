package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary ORDER BY date DESC")
    fun getAllDiaries(): Flow<List<DiaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(entry: DiaryEntity)

    @Update
    suspend fun updateDiary(entry: DiaryEntity)

    @Query("DELETE FROM diary WHERE id = :id")
    suspend fun deleteDiaryById(id: Long)
}

