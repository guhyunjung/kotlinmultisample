package com.example.kotlinmultisample.di

import android.content.Context
import androidx.room.Room
import com.example.kotlinmultisample.shared.data.local.database.AppDatabase
import com.example.kotlinmultisample.shared.data.local.datasource.LocalCountryDataSource
import com.example.kotlinmultisample.shared.data.local.datasource.LocalCountryDataSourceImpl
import com.example.kotlinmultisample.shared.data.repository.CountryRepositoryImpl
import com.example.kotlinmultisample.shared.di.networkModule
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository
import com.example.kotlinmultisample.shared.network.ConnectivityObserver
import com.example.kotlinmultisample.util.AndroidConnectivityObserver
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android 전용 Room 데이터베이스 Koin 모듈
 *
 * DB 파일 위치: /data/data/<패키지명>/databases/app_database.db
 */
val androidDatabaseModule = module {

    /** AppDatabase 싱글톤 등록 */
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database.db"
        )
            .fallbackToDestructiveMigration(true)
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
 * Android 전용 Koin 모듈 목록
 */
val androidModules = listOf(
    networkModule,         // Retrofit + OkHttp + CountryApiService + RemoteCountryDataSource
    androidDatabaseModule, // Room DB + CountryDao + LocalCountryDataSource
    viewModelModule,       // ViewModel 등록
    module {
        /**
         * CountryRepository → CountryRepositoryImpl 바인딩 (Android)
         *
         * RemoteCountryDataSource : networkModule에 등록
         * LocalCountryDataSource  : androidDatabaseModule에 등록
         */
        single<CountryRepository> {
            CountryRepositoryImpl(
                remoteDataSource = get(),
                localDataSource = get()
            )
        }

        single<ConnectivityObserver> {
            AndroidConnectivityObserver(androidContext())
        }

        single<ObservableSettings> {
            val sharedPrefs = androidContext().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            SharedPreferencesSettings(sharedPrefs)
        }
    }
)
