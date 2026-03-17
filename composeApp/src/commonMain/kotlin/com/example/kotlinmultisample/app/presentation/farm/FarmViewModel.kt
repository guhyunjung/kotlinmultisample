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
        if (broker == null) {
            farmRepository.getSeeds()
        } else {
            farmRepository.getSeedsByBroker(broker.id)
        }
    }.map { domainSeeds ->
        domainSeeds.map { it.toUiModel() }
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
    
    fun addSeed(seed: FarmSeed) {
        val broker = _selectedBroker.value ?: return
        viewModelScope.launch {
            farmRepository.addSeed(seed.toDomainModel(broker.id))
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

    /**
     * 증권사 이름 수정
     * @param broker 수정할 증권사 객체
     * @param newName 새로운 이름
     */
    fun updateBroker(broker: Broker, newName: String) {
        viewModelScope.launch {
            farmRepository.updateBroker(broker.copy(name = newName))
        }
    }

    /**
     * 증권사 삭제
     * @param broker 삭제할 증권사 객체
     */
    fun deleteBroker(broker: Broker) {
        viewModelScope.launch {
            // 1. 브로커 삭제
            farmRepository.deleteBroker(broker.name) 
            
            // 2. 선택된 브로커가 삭제된 브로커라면 선택 해제
            if (_selectedBroker.value?.id == broker.id) {
                _selectedBroker.value = null
            }
        }
    }

    private fun DomainFarmSeed.toUiModel(): FarmSeed {
        return FarmSeed(
            id = id,
            brokerId = brokerId,
            name = name,
            emoji = emoji,
            pct = pct,
            buyingPrice = buyingPrice,
            quantity = quantity
        )
    }

    private fun FarmSeed.toDomainModel(brokerId: Long): DomainFarmSeed {
        return DomainFarmSeed(
            id = id,
            brokerId = brokerId,
            name = name,
            emoji = emoji,
            pct = pct,
            buyingPrice = buyingPrice,
            quantity = quantity
        )
    }
}
