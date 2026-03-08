package com.example.kotlinmultisample.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.kotlinmultisample.shared.data.local.database.AppDatabase
import com.example.kotlinmultisample.shared.data.local.datasource.LocalCountryDataSource
import com.example.kotlinmultisample.shared.data.local.datasource.LocalCountryDataSourceImpl
import com.example.kotlinmultisample.shared.data.repository.CountryRepositoryImpl
import com.example.kotlinmultisample.shared.di.networkModule
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository
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
     */
    single<AppDatabase> {
        // DB 파일을 저장할 디렉토리를 생성합니다.
        val dbDir = File(System.getProperty("user.home"), ".kotlinmultisample").also { it.mkdirs() }
        val dbFile = File(dbDir, "app_database.db")

        Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            // TODO: 스키마 변경 시 아래와 같이 Migration을 추가하세요.
            // .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration(true) // 개발 중: 마이그레이션 없으면 DB 초기화
            // ※ JVM Desktop Room은 allowMainThreadQueries()를 지원하지 않습니다.
            //   DB 접근은 반드시 Coroutine(withContext(Dispatchers.IO))을 사용하세요.
            .build()
    }

    /** CountryDao 싱글톤 등록 */
    single { get<AppDatabase>().countryDao() }

    /**
     * LocalCountryDataSource → LocalCountryDataSourceImpl 바인딩
     *
     * Room CountryDao를 주입받아 로컬 캐시를 담당합니다.
     */
    single<LocalCountryDataSource> { LocalCountryDataSourceImpl(get()) }
}

/**
 * JVM(Desktop) 전용 Country Koin 모듈
 *
 * networkModule에 CountryApiService + RemoteCountryDataSource가 등록되어 있으므로
 * CountryRepository 바인딩만 추가합니다.
 */
val jvmCountryModule = module {
    /**
     * CountryRepository → CountryRepositoryImpl 바인딩 (JVM Desktop)
     *
     * RemoteCountryDataSource : networkModule에 등록
     * LocalCountryDataSource  : jvmDatabaseModule에 등록
     */
    single<CountryRepository> {
        CountryRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        )
    }
}

/**
 * JVM(Desktop) 전용 Koin 모듈 목록
 *
 * [com.example.kotlinmultisample.shared.di.initKoin]의
 * additionalModules 파라미터에 전달하여 사용합니다.
 *
 * 포함된 모듈:
 * - [networkModule]     : Retrofit + OkHttp + CountryApiService + RemoteCountryDataSource
 * - [jvmDatabaseModule] : Room 로컬 DB (JVM Desktop 전용)
 * - [jvmCountryModule]  : CountryRepository 바인딩 (JVM Desktop 전용)
 * - [viewModelModule]   : ViewModel 등록
 */
val jvmModules = listOf(
    networkModule,      // Retrofit + OkHttp + API Services + RemoteDataSources
    jvmDatabaseModule,  // Room Database
    jvmCountryModule,   // CountryRepository
    viewModelModule     // ViewModel 등록
)
