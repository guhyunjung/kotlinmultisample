import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.dokka)
}

kotlin {
	androidTarget {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_11)
		}
	}

	jvm()

	js {
		outputModuleName = "shared"
		browser()
		binaries.library()
		generateTypeScriptDefinitions()
		compilerOptions {
			target = "es2015"
		}
	}

	sourceSets {
		commonMain.dependencies {
			// 모든 플랫폼에서 공통으로 사용하는 의존성
			implementation(project.dependencies.platform(libs.koin.bom))
			implementation(libs.koin.core)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
			implementation(libs.koin.test)
		}
		// JVM(Desktop) 전용 의존성
		// Retrofit, OkHttp는 JVM 기반 라이브러리이므로 jvmMain에만 추가합니다.
		jvmMain.dependencies {
			// Retrofit: HTTP 통신 및 REST API 클라이언트
			implementation(libs.retrofit.core)
			// Gson Converter: JSON ↔ Kotlin 데이터 클래스 자동 변환
			implementation(libs.retrofit.converter.gson)
			// OkHttp: Retrofit의 기반 HTTP 클라이언트
			implementation(libs.okhttp.core)
			// OkHttp Logging Interceptor: 네트워크 요청/응답 로그 출력 (개발용)
			implementation(libs.okhttp.logging.interceptor)
		}
	}
}

android {
	namespace = "com.example.kotlinmultisample.shared"
	compileSdk = libs.versions.android.compileSdk.get().toInt()
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	defaultConfig {
		minSdk = libs.versions.android.minSdk.get().toInt()
	}
}
