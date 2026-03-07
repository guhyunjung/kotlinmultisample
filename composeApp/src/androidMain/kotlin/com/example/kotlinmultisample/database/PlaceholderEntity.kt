package com.example.kotlinmultisample.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 임시 placeholder Entity
 *
 * 실제 Entity 클래스를 추가한 후 이 파일을 삭제하고
 * AppDatabase의 entities에서도 제거하세요.
 */
@Entity(tableName = "placeholder")
data class PlaceholderEntity(
	/** 기본 키 (Room이 자동으로 증가시킵니다) */
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0
)

