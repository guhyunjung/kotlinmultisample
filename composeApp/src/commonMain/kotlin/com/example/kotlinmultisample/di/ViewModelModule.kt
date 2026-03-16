package com.example.kotlinmultisample.di

import com.example.kotlinmultisample.app.presentation.country.CountryViewModel
import com.example.kotlinmultisample.app.presentation.farm.FarmViewModel
import com.example.kotlinmultisample.app.presentation.settings.SettingsViewModel
import com.example.kotlinmultisample.simple.FruitRepository
import com.example.kotlinmultisample.simple.FruitViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * composeApp 공통 ViewModel Koin 모듈
 *
 * Android / JVM Desktop 양쪽에서 공통으로 사용하는 ViewModel을 등록합니다.
 * 각 플랫폼의 additionalModules에 이 모듈을 추가해야 합니다.
 */
val viewModelModule = module {

    /**
     * 간소화된 기능 (Simple Feature)
     * Repository와 ViewModel을 한 곳에 등록 (간결함 강조)
     */
    single { FruitRepository() }
    viewModel { FruitViewModel(get()) }

    // Theme 설정 (앱 전역 상태 공유를 위해 single로 등록)
    single { SettingsViewModel(get()) }

    // 농장 (Farm) - 튜토리얼 상태 유지 등을 위해 Single로 등록 (간단한 상태 관리)
    single { FarmViewModel(get(), get()) }

    /**
     * 국가 관련 (Domain Layer + Presentation Layer)
     * - Repository는 Shared 모듈에서 제공 (Use Case 생략 가능 -> 바로 ViewModel 주입)
     * - ViewModel은 Shared 모듈이 아닌 ComposeApp 모듈에서 정의 (UI 로직)
     * - Shared 모듈의 Use Case나 Repository를 생성자로 주입받음
     */
    viewModel { CountryViewModel(get(), get(), get()) }
}
