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
			// put your Multiplatform dependencies here
			implementation(project.dependencies.platform(libs.koin.bom))
			implementation(libs.koin.core)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
			implementation(libs.koin.test)
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
