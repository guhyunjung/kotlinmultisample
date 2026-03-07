package com.example.kotlinmultisample.shared.di

import com.example.kotlinmultisample.shared.data.repository.ProjectRepositoryImpl
import com.example.kotlinmultisample.shared.domain.interactor.ProjectInteractor
import com.example.kotlinmultisample.shared.domain.repository.ProjectRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Koin DI 초기화 함수 (공통 + 플랫폼별 추가 모듈 지원)
 *
 * @param additionalModules 플랫폼별로 추가 등록할 Koin 모듈 목록
 *   - JVM(Desktop): Retrofit, Room 등 JVM 전용 모듈 → shared/jvmMain/di/JvmModule.kt 참고
 *   - Android: Android 전용 모듈 → shared/androidMain/di/AndroidModule.kt 참고
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
 * iOS, JS 등 단순 초기화가 필요한 플랫폼에서 호출되는 오버로드
 * additionalModules 없이 공통 모듈만 등록됩니다.
 */
fun initKoin() = initKoin {}

/**
 * 모든 플랫폼에서 공통으로 사용되는 Koin 모듈
 *
 * ※ Retrofit, Room 등 JVM/Android 전용 라이브러리는 commonMain에 등록할 수 없으므로
 *   각 플랫폼의 additionalModules를 통해 주입합니다.
 */
val commonModule = module {

	/**
	 * ProjectRepository → ProjectRepositoryImpl 바인딩
	 *
	 * ProjectRepositoryImpl은 RemoteProjectDataSource를 주입받아
	 * 원격 서버와 통신합니다.
	 * - get(): Koin이 플랫폼별 networkModule에 등록된
	 *   RemoteProjectDataSource 구현체를 자동으로 주입합니다.
	 *
	 * ※ networkModule(JVM/Android)이 additionalModules에 반드시 포함되어야 합니다.
	 */
	single<ProjectRepository> {
		ProjectRepositoryImpl(get())
	}

	/**
	 * ProjectInteractor 싱글톤 등록
	 *
	 * get()을 통해 위에서 등록된 ProjectRepository를 자동 주입합니다.
	 * single { ... }: 앱 생명주기 동안 단 하나의 인스턴스만 유지됩니다.
	 */
	single {
		ProjectInteractor(get())
	}

	// factory { ... }: 요청할 때마다 새로운 인스턴스를 생성합니다.
}