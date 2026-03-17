package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.data.local.datasource.LocalFarmDataSource
import com.example.kotlinmultisample.shared.domain.model.Broker
import com.example.kotlinmultisample.shared.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow

/**
 * 농장 데이터 저장소 구현체
 *
 * 주로 LocalDataSource(Room)을 통해 데이터를 관리합니다.
 * 추후 서버 연동이 필요하면 RemoteDataSource를 주입받아 확장할 수 있습니다.
 */
class FarmRepositoryImpl(
    private val localDataSource: LocalFarmDataSource
) : FarmRepository {
    
    /**
     * 모든 증권사 목록 실시간 조회
     */
    override fun getBrokers(): Flow<List<Broker>> {
        return localDataSource.getBrokers()
    }

    /**
     * 증권사 추가 (ID는 DB에서 자동 생성되므로 0으로 전달)
     */
    override suspend fun addBroker(name: String) {
        localDataSource.insertBroker(Broker(id = 0, name = name))
    }

    /**
     * 증권사 삭제
     */
    override suspend fun deleteBroker(name: String) {
        localDataSource.deleteBrokerByName(name)
    }

    /**
     * 증권사 수정
     */
    override suspend fun updateBroker(broker: Broker) {
        localDataSource.updateBroker(broker)
    }
}
