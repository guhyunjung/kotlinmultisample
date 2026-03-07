import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.composeHotReload)
	alias(libs.plugins.dokka)
	alias(libs.plugins.ksp)
}

kotlin {
	// 기본 소스셋 계층 구조를 유지하면서 jvmAndroid 커스텀 그룹을 추가합니다.
	@OptIn(ExperimentalKotlinGradlePluginApi::class)
	applyDefaultHierarchyTemplate {
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

	jvm {
		// jvmRun task에서 사용할 메인 클래스 지정
		mainRun {
			mainClass = "com.example.kotlinmultisample.MainKt"
		}
	}

	sourceSets {
		// jvmAndroidMain: group("jvmAndroid")에 의해 자동 생성된 소스셋
		// by getting으로 참조하여 의존성만 추가합니다.
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
		androidMain.dependencies {
			implementation(libs.compose.uiToolingPreview)
			implementation(libs.androidx.activity.compose)
			implementation(project.dependencies.platform(libs.koin.bom))
			implementation(libs.koin.android)
			// Room (Android 전용 - room-ktx 포함)
			implementation(libs.androidx.room.runtime)
			implementation(libs.androidx.room.ktx)
		}
		commonMain.dependencies {
			implementation(libs.compose.runtime)
			implementation(libs.compose.foundation)
			implementation(libs.compose.material.icons.extended)
			implementation(libs.compose.material3)
			implementation(libs.compose.ui)
			implementation(libs.compose.components.resources)
			implementation(libs.compose.uiToolingPreview)
			implementation(libs.androidx.lifecycle.viewmodelCompose)
			implementation(libs.androidx.lifecycle.runtimeCompose)
			implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
			implementation(project.dependencies.platform(libs.koin.bom))
			implementation(libs.koin.compose)
			implementation(projects.shared)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
		}
		jvmMain.dependencies {
			implementation(compose.desktop.currentOs)
			implementation(libs.kotlinx.coroutinesSwing)
			// Room: SQLite 기반 로컬 DB
			// ※ room-ktx는 Android 전용(.aar)이므로 JVM Desktop에서는 제외합니다.
			implementation(libs.androidx.room.runtime)
		}
	}
}

android {
	namespace = "com.example.kotlinmultisample"
	compileSdk = libs.versions.android.compileSdk.get().toInt()

	defaultConfig {
		applicationId = "com.example.kotlinmultisample"
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()
		versionCode = 1
		versionName = "1.0"
	}
	packaging {
		resources {
		 excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
}

dependencies {
	debugImplementation(libs.compose.uiTooling)
	add("kspAndroid", libs.androidx.room.compiler) // Room 어노테이션 프로세서 (Android)
	add("kspJvm", libs.androidx.room.compiler)     // Room 어노테이션 프로세서 (JVM Desktop)
}

compose.desktop {
	application {
		mainClass = "com.example.kotlinmultisample.MainKt"

		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
			packageName = "com.example.kotlinmultisample"
			packageVersion = "1.0.0"
		}
	}
}
