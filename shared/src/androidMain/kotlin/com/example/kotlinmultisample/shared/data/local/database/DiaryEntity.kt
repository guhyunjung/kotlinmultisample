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
    // Room은 복잡한 객체나 Enum을 직접 저장하지 못하므로 컨버터가 필요하지만, 
    // 여기선 String으로 저장하거나 TypeConverter를 사용해야 함.
    // 간단하게 String으로 저장하고 변환.
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

