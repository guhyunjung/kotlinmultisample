package com.example.kotlinmultisample.shared.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Koin DI 초기화 함수 (공통 + 플랫폼별 추가 모듈 지원)
 *
 * @param additionalModules 플랫폼별로 추가 등록할 Koin 모듈 목록
 *   - JVM(Desktop): Retrofit, Room 등 JVM 전용 모듈 → composeApp/jvmMain/di/JvmModule.kt 참고
 *   - Android: Android 전용 모듈 → composeApp/androidMain/di/AndroidModule.kt 참고
 * @param appDeclaration Koin 앱 수준 설정 (로깅, 태그 등)
 */
fun initKoin(
	additionalModules: List<Module> = emptyList(),
	appDeclaration: KoinAppDeclaration = {}
) {
	startKoin {
		appDeclaration()
		// 공통 모듈 + 플랫폼별 추가 모듈을 합쳐서 등록
		modules(commonModule + additionalModules)
	}
}

/**
 * iOS, JS 등 추가 모듈이 필요 없는 플랫폼에서 호출하는 오버로드
 * 공통 모듈만 등록됩니다.
 */
fun initKoin() = initKoin(emptyList())

/**
 * 모든 플랫폼에서 공통으로 사용되는 Koin 모듈
 *
 * ※ Retrofit, Room 등 JVM/Android 전용 라이브러리는 commonMain에 등록할 수 없으므로
 *   각 플랫폼의 additionalModules를 통해 주입합니다.
 */
val commonModule = module {
	// 공통 도메인 레이어 바인딩은 이 곳에 추가하세요.
	// 플랫폼별 Repository/DataSource는 각 플랫폼 모듈에서 등록합니다.
}