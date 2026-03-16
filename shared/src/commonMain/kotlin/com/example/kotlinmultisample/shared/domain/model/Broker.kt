package com.example.kotlinmultisample.shared.domain.model

/**
 * 증권사(Broker) 도메인 모델
 *
 * 사용자가 등록한 증권사(포트폴리오 그룹)를 나타냅니다.
 *
 * @property id 증권사 고유 ID
 * @property name 증권사 이름 (표시용)
 */
data class Broker(
    val id: Long,
    val name: String
)
