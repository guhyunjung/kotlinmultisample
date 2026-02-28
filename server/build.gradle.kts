plugins {
	alias(libs.plugins.kotlinJvm)
	alias(libs.plugins.springBoot)
	alias(libs.plugins.springDependencyManagement)
	java
	application
	kotlin("plugin.spring")
}

group = "com.example.kotlinmultisample"
version = "1.0.0"
application {
	mainClass.set("com.example.kotlinmultisample.ApplicationKt")
}

dependencies {
	implementation(projects.shared)
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.actuator)
	implementation(libs.spring.boot.starter.data.jpa)
	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.kotlin.testJunit)
	implementation(kotlin("stdlib"))
	implementation(kotlin("reflect"))
	// PostgreSQL JDBC Driver
	runtimeOnly(libs.postgresql)
}
repositories {
	mavenCentral()
}