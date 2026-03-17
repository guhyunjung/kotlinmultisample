package com.example.kotlinmultisample.shared.data.local.datasource

import com.example.kotlinmultisample.shared.data.local.database.BrokerDao
import com.example.kotlinmultisample.shared.data.local.database.toDomain
import com.example.kotlinmultisample.shared.data.local.database.toEntity
import com.example.kotlinmultisample.shared.domain.model.Broker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import co.touchlab.kermit.Logger

/**
 * Android 로컬 데이터 소스 구현체
 *
 * Room DAO를 통해 DB에 접근하며, Entity <-> Domain 변환을 수행합니다.
 * DB 작업은 IO 스레드에서 실행되도록 withContext(Dispatchers.IO)를 사용합니다.
 */
class LocalFarmDataSourceImpl(
    private val brokerDao: BrokerDao
) : LocalFarmDataSource {
    private val logger = Logger.withTag("LocalFarmDataSourceImpl(Android)")

    override fun getBrokers(): Flow<List<Broker>> {
        logger.d { "getBrokers() 호출" }
        return brokerDao.getAllBrokers().map { entities -> 
            entities.map { it.toDomain() } 
        }
    }

    override suspend fun insertBroker(broker: Broker) {
        logger.d { "insertBroker(broker=$broker) 호출" }
        withContext(Dispatchers.IO) {
            brokerDao.insertBroker(broker.toEntity())
        }
    }

    override suspend fun deleteBrokerByName(name: String) {
        logger.d { "deleteBrokerByName(name=$name) 호출" }
        withContext(Dispatchers.IO) {
            brokerDao.deleteBrokerByName(name)
        }
    }

    override suspend fun updateBroker(broker: Broker) {
        logger.d { "updateBroker(broker=$broker) 호출" }
        withContext(Dispatchers.IO) {
            brokerDao.insertBroker(broker.toEntity())
        }
    }
}
