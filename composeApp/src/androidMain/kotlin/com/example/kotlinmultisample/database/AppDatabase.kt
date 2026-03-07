package com.example.kotlinmultisample.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room 로컬 데이터베이스 (Android)
 *
 * - entities: 관리할 Entity(테이블) 목록. 추가 시 버전도 올려야 합니다.
 * - version: 스키마 버전. 구조 변경 시 Migration 필요.
 * - exportSchema: CI/CD에서 스키마 변경 이력 추적 시 true로 설정.
 */
@Database(
	entities = [
		PlaceholderEntity::class // TODO: 실제 Entity로 교체 후 PlaceholderEntity 제거하세요.
	],
	version = 1,
	exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
	// TODO: DAO 인터페이스를 아래에 추가하세요.
	// abstract fun projectDao(): ProjectDao
}

