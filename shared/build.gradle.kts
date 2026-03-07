import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.dokka)
}

kotlin {
	// 기본 소스셋 계층 구조(commonMain → androidMain/jvmMain/jsMain 등)를 유지하면서
	// jvmAndroidMain 커스텀 중간 소스셋을 추가합니다.
	//
	// applyDefaultHierarchyTemplate { ... } 람다를 사용하면 기본 템플릿을 유지한 채로
	// 커스텀 그룹을 extend(확장)할 수 있습니다.
	@OptIn(ExperimentalKotlinGradlePluginApi::class)
	applyDefaultHierarchyTemplate {
		// common 그룹 아래에 jvmAndroid 그룹을 추가합니다.
		// jvmAndroid는 jvm과 android 타깃을 포함하는 중간 소스셋입니다.
		common {
			group("jvmAndroid") {
				withJvm()
				withAndroidTarget()
			}
		}
	}

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
		// jvmAndroidMain: applyDefaultHierarchyTemplate의 group("jvmAndroid")에 의해
		// 자동으로 생성된 소스셋입니다. (네이밍 규칙: <groupName>Main)
		val jvmAndroidMain by getting {
			dependencies {
				// Retrofit: HTTP 통신 및 REST API 클라이언트 (JVM + Android 공유)
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
