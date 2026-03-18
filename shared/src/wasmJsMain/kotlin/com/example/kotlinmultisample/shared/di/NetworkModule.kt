package com.example.kotlinmultisample.shared.di

import org.koin.dsl.module

/**
 * wasmJs 전용 네트워크 모듈 (빈 구현).
 * wasm 환경에서는 Retrofit/OkHttp를 사용하지 않으므로 빈 모듈로 둡니다.
 */
actual val networkModule = module {
    // 필요시 Ktor-js 같은 구현을 여기에 등록하세요.
}

