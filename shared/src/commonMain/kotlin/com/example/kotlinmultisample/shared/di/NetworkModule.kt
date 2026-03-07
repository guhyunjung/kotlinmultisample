package com.example.kotlinmultisample.shared.di

import org.koin.core.module.Module

/**
 * 플랫폼별 네트워크 Koin 모듈 (expect 선언) // 예상하다
 *
 * 각 플랫폼의 actual 구현에서 OkHttpClient, Retrofit, ApiService 등을 등록합니다.
 * - JVM Desktop: shared/jvmMain/di/NetworkModule.kt
 * - Android    : shared/androidMain/di/NetworkModule.kt
 */
expect val networkModule: Module

