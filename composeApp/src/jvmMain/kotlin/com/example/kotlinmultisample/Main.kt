package com.example.kotlinmultisample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import co.touchlab.kermit.Logger
import com.example.kotlinmultisample.di.jvmModules
import com.example.kotlinmultisample.shared.di.initKoin

/**
 * JVM(Desktop) 애플리케이션의 진입점(Entry Point)
 *
 * [application] 블록은 Compose for Desktop에서 제공하는
 * 애플리케이션 생명주기(Lifecycle)를 관리하는 스코프입니다.
 * 이 블록 내에서 Window를 생성하고 DI를 초기화합니다.
 */
fun main() = application {
	/**
	 * Koin DI(Dependency Injection) 초기화
	 *
	 * - [initKoin]은 shared 모듈에 정의된 공통 DI 설정을 시작합니다.
	 * - [jvmModules]에는 JVM 전용 의존성이 포함됩니다:
	 *   - networkModule: Retrofit + OkHttp 네트워크 설정
	 *   - jvmDatabaseModule: Room 로컬 DB 설정
	 *
	 * ※ DI 초기화는 반드시 UI 렌더링(Window 생성) 이전에 호출해야 의존성 주입이 올바르게 동작합니다.
	 */
	initKoin(additionalModules = jvmModules)

	/**
	 * 애플리케이션 메인 윈도우(Window) 생성
	 */
	val logger = Logger.withTag("Main")
	Window(
		onCloseRequest = ::exitApplication,
		title = "KotlinMultiSample",
	) {
		App(
			onExit = ::exitApplication,
			showToast = { logger.i { "Toast: $it" } }
		)
	}
}
