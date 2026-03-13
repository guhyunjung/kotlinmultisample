package com.example.kotlinmultisample.util

import com.example.kotlinmultisample.shared.network.ConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.InetAddress

/**
 * JVM(Desktop) 플랫폼을 위한 네트워크 연결 상태 관찰자입니다.
 * Android와 같은 시스템 레벨의 네트워크 콜백이 없으므로,
 * 주기적으로 특정 서버(Google DNS)에 핑(Reachable Check)을 보내 연결 상태를 확인합니다.
 */
class JvmConnectivityObserver: ConnectivityObserver {

    /**
     * 주기적으로(5초마다) 네트워크 상태를 체크하여 Flow로 방출합니다.
     * 
     * 주의: InetAddress.getByName().isReachable()은 네트워크 작업을 수행하므로
     * 반드시 Main Thread가 아닌 IO Thread에서 실행되어야 합니다. (flowOn(Dispatchers.IO) 사용)
     */
    override fun observe(): Flow<ConnectivityObserver.Status> = flow {
        while(true) {
            val isConnected = try {
                // 구글 DNS(8.8.8.8) 서버에 핑 체크 (타임아웃 1500ms 설정)
                // 실제 인터넷 연결 가능 여부를 가장 확실하게 판단하는 방법 중 하나입니다.
                InetAddress.getByName("8.8.8.8").isReachable(1500)
            } catch(e: Exception) {
                // 예외 발생(SocketTimeout 등) 시 연결 실패로 간주
                false
            }
            
            // 연결 성공 여부에 따라 상태 방출
            emit(if(isConnected) ConnectivityObserver.Status.Available else ConnectivityObserver.Status.Lost)
            
            // 주기적 체크 간격 (5초)
            delay(5000L) 
        }
    }.flowOn(Dispatchers.IO) // 이 Flow의 모든 업스트림(위쪽 코드들)을 IO 스레드에서 실행하도록 지정
}
