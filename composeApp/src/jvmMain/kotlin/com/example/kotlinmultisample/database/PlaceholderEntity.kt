package com.example.kotlinmultisample.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 임시 placeholder Entity
 *
 * Room의 @Database는 entities 목록이 비어있으면 KSP 컴파일 오류가 발생합니다.
 * 실제 Entity 클래스를 추가한 후 이 클래스는 제거하세요.
 *
 * TODO: 실제 Entity 클래스 추가 후 이 파일을 삭제하고 AppDatabase의 entities에서도 제거하세요.
 */
@Entity(tableName = "placeholder")
data class PlaceholderEntity(
	/** 기본 키 (Room이 자동으로 증가시킵니다) */
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0
)

