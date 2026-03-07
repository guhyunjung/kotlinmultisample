package com.example.kotlinmultisample.di

import androidx.room.Room
import com.example.kotlinmultisample.database.AppDatabase
import com.example.kotlinmultisample.shared.di.networkModule
import org.koin.dsl.module
import java.io.File

/**
 * JVM(Desktop) 전용 Room 데이터베이스 Koin 모듈
 *
 * Android와 달리 JVM Desktop 환경에서는 Context가 없으므로
 * 파일 시스템 경로를 직접 지정하여 DB 파일을 생성합니다.
 */
val jvmDatabaseModule = module {

	/**
	 * AppDatabase 싱글톤 등록
	 *
	 * - DB 파일 위치: 사용자 홈 디렉토리 하위 .kotlinmultisample/app_database.db
	 *   예) Windows: C:\Users\<사용자명>\.kotlinmultisample\app_database.db
	 *       macOS/Linux: ~/.kotlinmultisample/app_database.db
	 * - allowMainThreadQueries(): 개발 편의를 위해 메인 스레드에서 DB 접근을 허용합니다.
	 *   → 운영 환경에서는 제거하고 Coroutine(suspend 함수)을 사용하세요.
	 */
	single<AppDatabase> {
		// DB 파일을 저장할 디렉토리를 생성합니다.
		val dbDir = File(System.getProperty("user.home"), ".kotlinmultisample").also { it.mkdirs() }
		val dbFile = File(dbDir, "app_database.db")

		Room.databaseBuilder<AppDatabase>(
			name = dbFile.absolutePath
		)
			// TODO: 스키마 변경 시 아래와 같이 Migration을 추가하세요.
			// .addMigrations(MIGRATION_1_2)
			.fallbackToDestructiveMigration(true) // 개발 중: 마이그레이션 없으면 DB 초기화
			// ※ JVM Desktop Room은 allowMainThreadQueries()를 지원하지 않습니다.
			//   DB 접근은 반드시 Coroutine(withContext(Dispatchers.IO))을 사용하세요.
			.build()
	}

	/**
	 * ProjectDao 싱글톤 등록
	 *
	 * AppDatabase 인스턴스에서 DAO를 꺼내 Koin에 등록합니다.
	 * get<AppDatabase>()으로 위에서 등록한 DB 인스턴스를 자동 주입합니다.
	 */
	single { get<AppDatabase>().projectDao() }
}

/**
 * JVM(Desktop) 전용 Koin 모듈 목록
 *
 * [com.example.kotlinmultisample.shared.di.initKoin]의
 * additionalModules 파라미터에 전달하여 사용합니다.
 *
 * 포함된 모듈:
 * - [networkModule]: Retrofit + OkHttp + ProjectApiService + RemoteProjectDataSource (shared/jvmMain 정의)
 * - [jvmDatabaseModule]: Room 로컬 DB + ProjectDao (JVM Desktop 전용)
 */
val jvmModules = listOf(
	networkModule,    // Retrofit + OkHttp + API Service + RemoteDataSource
	jvmDatabaseModule // Room Database + ProjectDao
)



