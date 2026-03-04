package com.example.kotlinmultisample.shared.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule)
}

// iOS 등에서 호출됨
fun initKoin() = initKoin {}

val commonModule = module {
    // 여기에 종속성을 추가하세요.
    // single { "Hello Koin" }
}

