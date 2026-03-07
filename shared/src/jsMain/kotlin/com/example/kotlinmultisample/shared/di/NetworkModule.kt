package com.example.kotlinmultisample.shared.di

import org.koin.dsl.module

/**
 * JS 플랫폼 네트워크 Koin 모듈 (actual 구현)
 *
 * JS 환경에서는 Retrofit/OkHttp를 사용하지 않습니다.
 * JS 네트워크 통신이 필요한 경우 Ktor-js 등을 사용하세요.
 * expect 선언을 충족하기 위한 빈 모듈입니다.
 */
actual val networkModule = module {
    // JS 플랫폼에서는 Retrofit을 사용하지 않으므로 비워둡니다.
}

