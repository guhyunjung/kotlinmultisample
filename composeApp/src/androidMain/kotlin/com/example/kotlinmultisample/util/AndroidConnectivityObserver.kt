package com.example.kotlinmultisample.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.example.kotlinmultisample.shared.network.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Android 플랫폼 전용 네트워크 연결 상태 구현체입니다.
 * Android ConnectivityManager의 콜백을 활용하여 실시간 네트워크 상태를 모니터링합니다.
 */
class AndroidConnectivityObserver(
    private val context: Context
): ConnectivityObserver {

    // 시스템 연결 서비스(ConnectivityManager) 인스턴스
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * callbackFlow를 사용하여 비동기 콜백 이벤트를 Kotlin Flow로 변환합니다.
     */
    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            // 네트워크 상태 변화를 수신하는 콜백 정의
            val callback = object : ConnectivityManager.NetworkCallback() {
                // 네트워크가 사용 가능해졌을 때
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(ConnectivityObserver.Status.Available) }
                }

                // 네트워크가 불안정하거나 끊어지고 있을 때
                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(ConnectivityObserver.Status.Losing) }
                }

                // 네트워크 연결이 완전히 끊겼을 때
                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(ConnectivityObserver.Status.Lost) }
                }

                // 네트워크를 찾을 수 없을 때
                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(ConnectivityObserver.Status.Unavailable) }
                }
            }

            // 시스템에 네트워크 콜백 등록 (앱 종료 시 명시적으로 해제해야 함)
            connectivityManager.registerDefaultNetworkCallback(callback)
            
            // Flow 수집이 끝날 때(awaitClose) 실행되는 블록: 콜백 등록 해제
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged() // 상태가 실제로 변경되었을 때만 방출 (중복 제거)
    }
}
