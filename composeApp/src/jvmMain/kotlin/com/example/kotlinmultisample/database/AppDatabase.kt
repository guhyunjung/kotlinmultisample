package com.example.kotlinmultisample.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room 로컬 데이터베이스 추상 클래스
 *
 * @Database 어노테이션으로 Room에 DB 설정을 알립니다.
 * - entities: 이 DB에서 관리할 Entity(테이블) 목록
 *   → Entity 추가 시 이 목록에 함께 추가해주세요.
 * - version: DB 스키마 버전. 테이블 구조가 변경될 때마다 올려야 합니다.
 *   → 버전 변경 시 Migration 객체도 함께 작성해야 합니다.
 * - exportSchema: 스키마를 JSON 파일로 내보낼지 여부
 *   → CI/CD 환경에서 스키마 변경 이력을 추적하려면 true로 설정하세요.
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
	// Room이 자동으로 구현체를 생성합니다.
	// 예시:
	// abstract fun projectDao(): ProjectDao
}


