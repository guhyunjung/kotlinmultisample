package com.example.kotlinmultisample

import android.app.Application
import com.example.kotlinmultisample.di.androidModules
import com.example.kotlinmultisample.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

/**
 * Android 애플리케이션의 진입점 클래스입니다.
 * 전역 상태를 관리하고, 앱 시작 시 Koin 의존성 주입 프레임워크를 초기화합니다.
 */
class MainApplication : Application() {
	/**
	 * 애플리케이션이 생성될 때 호출됩니다.
	 * 상위 클래스의 초기화를 수행한 후, Android 컨텍스트와 로거를 포함하여 Koin을 설정합니다.
	 */
	override fun onCreate() {
		super.onCreate()

		// Android 전용 모듈(Network + Database)을 포함하여 Koin 초기화
		// androidModules = listOf(networkModule, androidDatabaseModule)
		initKoin(
			additionalModules = androidModules
		) {
			androidLogger()
			androidContext(this@MainApplication)
		}
	}
}