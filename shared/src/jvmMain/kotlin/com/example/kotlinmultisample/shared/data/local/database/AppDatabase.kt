package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room 로컬 데이터베이스 (JVM Desktop)
 *
 * - entities: 관리할 Entity(테이블) 목록. 추가 시 버전도 올려야 합니다.
 * - version: 스키마 버전. 구조 변경 시 Migration 필요.
 * - exportSchema: CI/CD에서 스키마 변경 이력 추적 시 true로 설정.
 */
@Database(
    entities = [
        CountryEntity::class,  // 국가 정보 캐시 테이블
        BrokerEntity::class,   // 브로커 정보 캐시 테이블
        FarmSeedEntity::class  // 농작물(주식) 테이블
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 국가 DAO 접근자
     *
     * Room이 컴파일 타임에 [CountryDao] 구현체를 자동 생성합니다.
     * Koin 모듈에서 single { get<AppDatabase>().countryDao() } 로 등록하세요.
     */
    abstract fun countryDao(): CountryDao

    /**
     * 브로커 DAO 접근자
     *
     * Room이 컴파일 타임에 [BrokerDao] 구현체를 자동 생성합니다.
     * Koin 모듈에서 single { get<AppDatabase>().brokerDao() } 로 등록하세요.
     */
    abstract fun brokerDao(): BrokerDao

    /**
     * 농작물 DAO 접근자
     *
     * Room이 컴파일 타임에 [FarmSeedDao] 구현체를 자동 생성합니다.
     * Koin 모듈에서 single { get<AppDatabase>().farmSeedDao() } 로 등록하세요.
     */
    abstract fun farmSeedDao(): FarmSeedDao
}
