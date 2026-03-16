package com.example.kotlinmultisample.app.presentation.farm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmultisample.shared.domain.model.Broker
import com.example.kotlinmultisample.shared.domain.repository.FarmRepository
import com.example.kotlinmultisample.shared.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import com.example.kotlinmultisample.app.ui.screen.farm.FarmSeed
import com.example.kotlinmultisample.app.ui.screen.farm.SummaryData

/**
 * 농장 화면의 상태를 관리하는 ViewModel
 *
 * - 튜토리얼 표시 여부 제어
 * - 증권사(Broker) 목록 관리 및 선택
 * - Koin으로 의존성을 주입받습니다. [com.example.kotlinmultisample.di.viewModelModule]
 */
class FarmViewModel(
    private val settingsRepository: SettingsRepository,
    private val farmRepository: FarmRepository
) : ViewModel() {

    /**
     * 튜토리얼 표시 여부 (StateFlow)
     *
     * - [isTutorialCompleted]: 튜토리얼을 완료하지 않았고
     * - [isTutorialEnabled]: 설정에서 튜토리얼 보기가 켜져있을 때만 true
     */
    val showTutorial = combine(
        settingsRepository.isTutorialCompleted(),
        settingsRepository.isTutorialEnabled()
    ) { completed, enabled ->
        !completed && enabled
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // 증권사 목록 (내부 MutableStateFlow / 외부 StateFlow)
    private val _brokers = MutableStateFlow<List<Broker>>(emptyList())
    /** 실시간 증권사 목록 */
    val brokers = _brokers.asStateFlow()

    // 현재 선택된 증권사 (null이면 '전체')
    private val _selectedBroker = MutableStateFlow<Broker?>(null)
    /** 현재 선택된 증권사 (필터링 기준) */
    val selectedBroker = _selectedBroker.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentSeeds = selectedBroker.flatMapLatest { broker ->
        flowOf(getFakeSeeds(broker?.id))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val summaryInfo = currentSeeds.map { seeds ->
        val totalInvest = seeds.sumOf { it.totalBuyingValue }
        val totalCurrent = seeds.sumOf { it.totalCurrentValue }
        val totalProfit = totalCurrent - totalInvest
        
        SummaryData(totalInvest, totalCurrent, totalProfit)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SummaryData())

    init {
        loadBrokers()
    }

    private fun getFakeSeeds(brokerId: Long?): List<FarmSeed> {
        // 더미 데이터 생성 시뮬레이션
        if (brokerId == null) {
            // 전체 보기 (모든 데이터 합침)
            return listOf(
                FarmSeed("한화에어로", "🌾", 10.5, 50000.0, 10),
                FarmSeed("삼성전자", "🌿", 8.4, 70000.0, 5),
                FarmSeed("카카오", "🍂", -3.2, 55000.0, 8),
                FarmSeed("NAVER", "🌱", 5.1, 180000.0, 2),
                FarmSeed("LG에너지", "🥀", -11.2, 400000.0, 1),
                FarmSeed("SK하이닉스", "🎋", 15.3, 120000.0, 3)
            )
        }

        // 브로커 ID에 따라 다른 데이터 반환 (임의의 규칙 적용)
        return when (brokerId % 3) {
            0L -> listOf( // 키움증권 스타일
                FarmSeed("한화에어로", "🌾", 10.5, 50000.0, 10),
                FarmSeed("현대로템", "🚂", 22.1, 35000.0, 20),
                FarmSeed("LIG넥스원", "🚀", 5.4, 98000.0, 5)
            )
            1L -> listOf( // 토스증권 스타일
                FarmSeed("삼성전자", "🌿", 8.4, 70000.0, 5),
                FarmSeed("카카오", "🍂", -3.2, 55000.0, 8),
                FarmSeed("NAVER", "🌱", 5.1, 180000.0, 2)
            )
            else -> listOf( // 나무증권 스타일
                FarmSeed("LG에너지", "🥀", -11.2, 400000.0, 1),
                FarmSeed("POSCO홀딩스", "🏭", -1.5, 450000.0, 2)
            )
        }
    }

    /**
     * 초기 증권사 목록 로드
     * Repository의 Flow를 구독하여 DB 변경 사항을 실시간 반영합니다.
     */
    private fun loadBrokers() {
        viewModelScope.launch {
            farmRepository.getBrokers()
                .catch { /* 에러 처리: 필요시 Snackbar 메시지 등으로 확장 가능 */ }
                .collect { list ->
                    _brokers.value = list
                }
        }
    }

    /**
     * 새로운 증권사 추가
     *
     * @param name 추가할 증권사 이름
     */
    fun addBroker(name: String) {
        viewModelScope.launch {
            farmRepository.addBroker(name)
        }
    }

    /**
     * 탭에서 증권사 선택
     *
     * @param broker 선택된 증권사 객체 (null이면 전체 선택)
     */
    fun selectBroker(broker: Broker?) {
        _selectedBroker.value = broker
    }

    /**
     * 튜토리얼 완료 처리
     * 사용자가 튜토리얼을 끝까지 보거나 건너뛰기 했을 때 호출
     */
    fun completeTutorial() {
        viewModelScope.launch {
            settingsRepository.setTutorialCompleted(true)
        }
    }
}
