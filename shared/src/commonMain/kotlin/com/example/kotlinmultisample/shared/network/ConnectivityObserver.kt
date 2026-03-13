package com.example.kotlinmultisample.shared.network

import kotlinx.coroutines.flow.Flow

/**
 * 네트워크 연결 상태를 관찰하는 인터페이스입니다.
 * 플랫폼(Android, JVM 등)마다 구현 방식이 다르므로 공통 인터페이스로 정의합니다.
 */
interface ConnectivityObserver {
    /**
     * 실시간 네트워크 상태 변경을 방출(emit)하는 Flow를 반환합니다.
     */
    fun observe(): Flow<Status>

    /**
     * 네트워크 연결 상태 열거형
     */
    enum class Status {
        Available,   // 인터넷 사용 가능
        Unavailable, // 인터넷 사용 불가 (연결 없음)
        Losing,      // 연결이 끊어지는 중 (불안정)
        Lost         // 연결 끊김
    }
}
