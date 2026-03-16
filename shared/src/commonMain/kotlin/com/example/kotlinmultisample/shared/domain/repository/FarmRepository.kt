package com.example.kotlinmultisample.shared.domain.repository

import com.example.kotlinmultisample.shared.domain.model.Broker
import kotlinx.coroutines.flow.Flow

/**
 * 농장(포트폴리오) 관련 데이터 저장소 인터페이스입니다.
 * 증권사 목록 및 작물(주식/코인) 관리 기능을 제공합니다.
 */
interface FarmRepository {
    /**
     * 저장된 모든 증권사 목록을 실시간으로 가져옵니다.
     * Room DB의 Flow를 구독하여 데이터 변경 시 자동으로 업데이트됩니다.
     *
     * @return [Broker] 리스트를 발행하는 Flow
     */
    fun getBrokers(): Flow<List<Broker>>

    /**
     * 새로운 증권사를 목록에 추가합니다.
     *
     * @param name 추가할 증권사 이름 (예: "토스증권", "키움증권")
     */
    suspend fun addBroker(name: String)

    /**
     * 특정 증권사를 목록에서 제거합니다.
     *
     * @param name 삭제할 증권사 이름
     */
    suspend fun deleteBroker(name: String)
}
