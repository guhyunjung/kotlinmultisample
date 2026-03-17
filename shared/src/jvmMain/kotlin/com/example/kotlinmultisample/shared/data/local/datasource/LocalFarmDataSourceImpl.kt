package com.example.kotlinmultisample.shared.data.local.datasource

import com.example.kotlinmultisample.shared.data.local.database.BrokerDao
import com.example.kotlinmultisample.shared.data.local.database.FarmSeedDao
import com.example.kotlinmultisample.shared.data.local.database.toDomain
import com.example.kotlinmultisample.shared.data.local.database.toEntity
import com.example.kotlinmultisample.shared.domain.model.Broker
import com.example.kotlinmultisample.shared.domain.model.FarmSeed
import co.touchlab.kermit.Logger
import com.example.kotlinmultisample.getPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * JVM Desktop 로컬 데이터 소스 구현체
 *
 * Room DAO를 통해 DB에 접근하며, Entity <-> Domain 변환을 수행합니다.
 * DB 작업은 IO 스레드에서 실행되도록 withContext(Dispatchers.IO)를 사용합니다.
 */
class LocalFarmDataSourceImpl(
    private val brokerDao: BrokerDao,
    private val farmSeedDao: FarmSeedDao
) : LocalFarmDataSource {
    private val logger = Logger.withTag("[${getPlatform().name}][LocalFarmDataSourceImpl]")

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
            // REPLACE 전략 사용으로 insertBroker 재사용 또는 명시적 update 쿼리 사용
            // 여기서는 Dao의 insert(REPLACE) 활용
            brokerDao.insertBroker(broker.toEntity())
        }
    }

    // --- Seed 관련 구현 ---

    override fun getSeeds(): Flow<List<FarmSeed>> {
        return farmSeedDao.getAllSeeds().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getSeedsByBroker(brokerId: Long): Flow<List<FarmSeed>> {
        return farmSeedDao.getSeedsByBroker(brokerId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertSeed(seed: FarmSeed) {
        withContext(Dispatchers.IO) {
            farmSeedDao.insertSeed(seed.toEntity())
        }
    }

    override suspend fun updateSeed(seed: FarmSeed) {
        withContext(Dispatchers.IO) {
            farmSeedDao.updateSeed(seed.toEntity())
        }
    }

    override suspend fun deleteSeed(seed: FarmSeed) {
        withContext(Dispatchers.IO) {
            farmSeedDao.deleteSeed(seed.toEntity())
        }
    }
}
