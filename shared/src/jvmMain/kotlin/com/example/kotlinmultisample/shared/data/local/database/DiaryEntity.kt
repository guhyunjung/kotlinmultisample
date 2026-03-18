package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinmultisample.shared.domain.model.Diary
import com.example.kotlinmultisample.shared.domain.model.DiaryType

@Entity(tableName = "diary")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val content: String,
    val type: String 
)

fun DiaryEntity.toDomain(): Diary {
    return Diary(
        id = id,
        date = date,
        content = content,
        type = try { 
                DiaryType.entries.find { it.name == type } ?: DiaryType.DAILY 
            } catch (e: Exception) { 
                DiaryType.DAILY 
            }
    )
}

fun Diary.toEntity(): DiaryEntity {
    return DiaryEntity(
        id = id,
        date = date,
        content = content,
        type = type.name
    )
}

