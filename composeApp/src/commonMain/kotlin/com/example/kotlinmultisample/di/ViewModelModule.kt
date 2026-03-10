package com.example.kotlinmultisample.di

import com.example.kotlinmultisample.app.presentation.country.CountryViewModel
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
     * CountryViewModel 등록
     *
     * UseCase는 shared commonModule에서 등록됩니다.
     * Koin이 자동으로 GetCountriesUseCase, RefreshCountriesUseCase,
     * SearchCountriesUseCase를 주입합니다.
     */
    viewModel { CountryViewModel(get(), get(), get()) }
}
