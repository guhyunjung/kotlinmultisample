import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.composeHotReload)
	alias(libs.plugins.dokka)
}

kotlin {
	androidTarget {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_11)
		}
	}

	jvm {
		mainRun {
			mainClass = "com.example.kotlinmultisample.MainKt"
		}
	}

	sourceSets {
		androidMain.dependencies {
			implementation(libs.compose.uiToolingPreview)
			implementation(libs.androidx.activity.compose)
			implementation(libs.kotlinx.coroutines.android) // Coroutines Android
			implementation(project.dependencies.platform(libs.koin.bom))
			implementation(libs.koin.android)
            implementation(libs.multiplatform.settings) // Add this
			// Room - DI 모듈에서 Room.databaseBuilder() 사용
			implementation(libs.androidx.room.runtime)
			implementation(libs.androidx.room.ktx)
			// Retrofit + OkHttp
			implementation(libs.retrofit.core)
			implementation(libs.retrofit.converter.gson)
			implementation(libs.okhttp.core)
			implementation(libs.okhttp.logging.interceptor)
		}
		commonMain.dependencies {
			implementation(libs.compose.runtime)
			implementation(libs.compose.foundation)
			implementation(libs.kotlinx.coroutines.core) // Coroutines Core
			implementation(libs.compose.material.icons.extended)
			implementation(libs.compose.material3)
			implementation(libs.compose.ui)
			implementation(libs.compose.components.resources)
			implementation(libs.compose.uiTooling)
			implementation(libs.compose.uiToolingPreview)
			implementation(libs.androidx.lifecycle.viewmodelCompose)
			implementation(libs.androidx.lifecycle.runtimeCompose)
			implementation(project.dependencies.platform(libs.koin.bom))
			implementation(libs.koin.compose)
			implementation(libs.koin.compose.viewmodel)
			implementation(projects.shared)
			implementation(libs.kermit)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
		}
		jvmMain.dependencies {
			implementation(compose.desktop.currentOs)
			implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.multiplatform.settings) // Add this
			// Room - DI 모듈에서 Room.databaseBuilder() 사용
			implementation(libs.androidx.room.runtime)
			implementation(libs.androidx.sqlite.bundled)
			// Retrofit + OkHttp
			implementation(libs.retrofit.core)
			implementation(libs.retrofit.converter.gson)
			implementation(libs.okhttp.core)
			implementation(libs.okhttp.logging.interceptor)
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
	// Room KSP는 shared 모듈에서 처리하므로 여기서는 제거
}

tasks.withType<JavaExec> {
    jvmArgs("-Dfile.encoding=UTF-8")
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
