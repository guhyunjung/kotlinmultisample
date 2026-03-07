package com.example.kotlinmultisample.di

import androidx.room.Room
import com.example.kotlinmultisample.database.AppDatabase
import com.example.kotlinmultisample.shared.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android 전용 Room 데이터베이스 Koin 모듈
 *
 * JVM(Desktop)과 달리 Android는 Context를 통해 DB 경로를 관리하므로
 * androidContext()를 사용하여 DB를 생성합니다.
 * DB 파일 위치: /data/data/<패키지명>/databases/app_database.db
 */
val androidDatabaseModule = module {

	/**
	 * AppDatabase 싱글톤 등록 (Android)
	 *
	 * - androidContext(): Koin에 등록된 Android Context를 자동 주입합니다.
	 *   → MainApplication에서 androidContext(this)로 등록된 값입니다.
	 * - fallbackToDestructiveMigration(true): 마이그레이션 없으면 DB 초기화
	 *   → 운영 환경에서는 반드시 Migration을 작성하세요.
	 */
	single<AppDatabase> {
		Room.databaseBuilder(
			androidContext(),
			AppDatabase::class.java,
			"app_database.db"
		)
			// TODO: 스키마 변경 시 아래와 같이 Migration을 추가하세요.
			// .addMigrations(MIGRATION_1_2)
			.fallbackToDestructiveMigration(true)
			.build()
	}

	// ─────────────────────────────────────────────────────────────
	// DAO 등록 예시
	// AppDatabase에서 DAO 추상 메서드를 선언한 후 아래와 같이 등록하세요.
	//
	// single { get<AppDatabase>().projectDao() }
	// ─────────────────────────────────────────────────────────────
}

/**
 * Android 전용 Koin 모듈 목록
 *
 * MainApplication의 initKoin(additionalModules = androidModules)에 전달합니다.
 *
 * 포함된 모듈:
 * - [networkModule]: Retrofit, OkHttp 네트워크 모듈 (shared/androidMain 정의)
 * - [androidDatabaseModule]: Room 로컬 DB 모듈 (Android 전용, Context 기반)
 */
val androidModules = listOf(
	networkModule,         // Retrofit + OkHttp
	androidDatabaseModule  // Room Database (Context 기반)
)
