package com.example.kotlinmultisample.di

import com.example.kotlinmultisample.app.presentation.country.CountryViewModel
import com.example.kotlinmultisample.app.presentation.project.ProjectViewModel
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
     * ProjectViewModel 등록
     * ProjectInteractor를 주입받습니다 (commonModule에 등록됨).
     */
    viewModel { ProjectViewModel(get()) }

    /**
     * CountryViewModel 등록
     * CountryRepository를 주입받습니다.
     * Android    : androidModules에 CountryRepository가 등록됨
     * JVM Desktop: jvmCountryModule에 CountryRepository가 등록됨
     */
    viewModel { CountryViewModel(get()) }
}


