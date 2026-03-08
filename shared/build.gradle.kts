import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.dokka)
	alias(libs.plugins.ksp)  // Room 어노테이션 프로세서용
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
			implementation(libs.kotlinx.coroutines.test)
		}
		// Retrofit, OkHttp, Room은 JS에서 사용할 수 없으므로
		// jvmMain, androidMain에 각각 추가합니다.
		jvmMain.dependencies {
			implementation(libs.retrofit.core)
			implementation(libs.retrofit.converter.gson)
			implementation(libs.okhttp.core)
			implementation(libs.okhttp.logging.interceptor)
			// Room (JVM Desktop)
			implementation(libs.androidx.room.runtime)
		}
		androidMain.dependencies {
			implementation(libs.retrofit.core)
			implementation(libs.retrofit.converter.gson)
			implementation(libs.okhttp.core)
			implementation(libs.okhttp.logging.interceptor)
			// Room (Android)
			implementation(libs.androidx.room.runtime)
			implementation(libs.androidx.room.ktx)
		}
		// JVM 전용 테스트: 실제 Retrofit API 호출 통합 테스트
		jvmTest.dependencies {
			implementation(libs.kotlin.test)
			implementation(libs.kotlinx.coroutines.test)
			implementation(libs.mockk)
			implementation(libs.retrofit.core)
			implementation(libs.retrofit.converter.gson)
			implementation(libs.okhttp.core)
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

// Room 어노테이션 프로세서 (KSP)
dependencies {
	add("kspAndroid", libs.androidx.room.compiler)  // Android용 Room 코드 생성
	add("kspJvm", libs.androidx.room.compiler)      // JVM Desktop용 Room 코드 생성
}
