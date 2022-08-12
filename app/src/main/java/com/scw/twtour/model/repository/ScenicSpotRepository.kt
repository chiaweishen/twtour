package com.scw.twtour.model.repository

import com.scw.twtour.util.City
import com.scw.twtour.network.util.ODataSelect
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.datasource.local.ScenicSpotPreferencesDataSource
import com.scw.twtour.model.datasource.remote.ScenicSpotRemoteDataSource
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber

interface ScenicSpotRepository {
    val syncState: StateFlow<SyncState>
    suspend fun syncScenicSpotData(key: SyncKey?)
    suspend fun syncScenicSpotComplete()
    fun getLastSyncScenicSpotTime(): Long
    fun setLastSyncScenicSpotTime(t: Long)
    fun getSyncCycleDays(): Int
}

class ScenicSpotRepositoryImpl(
    private val remoteDataSource: ScenicSpotRemoteDataSource,
    private val localDataSource: ScenicSpotLocalDataSource,
    private val preferencesDataSource: ScenicSpotPreferencesDataSource
) : ScenicSpotRepository {

    private val _syncState = MutableStateFlow<SyncState>(SyncNone)
    override val syncState = _syncState.asStateFlow()

    private var syncStartTime: Long = 0

    companion object {
        private const val LOAD_COUNT = 300
    }

    override suspend fun syncScenicSpotData(key: SyncKey?) {
        val city = key?.city ?: run {
            syncStartTime = System.currentTimeMillis()
            _syncState.emit(SyncStart)
            localDataSource.deleteAll()
            City.values()[0]
        }
        val skip = key?.skip ?: 0
        val cityCount = City.values().size
        val cityIndex = City.values().indexOf(city)


        /**
         * 資料約 4135kb (2022/8)
         * **/
        remoteDataSource.getScenicSpotsByCity(
            city, LOAD_COUNT, skip,
            ODataSelect.Builder()
                .add(ScenicSpotEntityItem::scenicSpotID.name)
                .add(ScenicSpotEntityItem::scenicSpotName.name)
                .add(ScenicSpotEntityItem::description.name)
                .add(ScenicSpotEntityItem::zipCode.name)
                .add(ScenicSpotEntityItem::position.name)
                .add(ScenicSpotEntityItem::picture.name)
                .build()
        )

            .onEach {
                migrateScenicSpotCity(it, city)
            }
            .flowOn(Dispatchers.IO)
            .catch { e ->
                _syncState.emit(SyncError(Exception(e)))
            }
            .collect {
                localDataSource.cache(it)

                if (it.size < LOAD_COUNT) {
                    val isLastCity = cityIndex + 1 >= cityCount
                    if (isLastCity) {
                        saveLastSyncTime()
                        syncScenicSpotComplete()
                        Timber.i("Sync all scenic spots duration: ${System.currentTimeMillis() - syncStartTime} ms")
                    } else {
                        val nextCity = City.values()[cityIndex + 1]
                        _syncState.emit(SyncProgress((cityIndex + 1) * 100f / cityCount, nextCity))
                        syncScenicSpotData(SyncKey(nextCity, 0))
                    }
                } else {
                    _syncState.emit(SyncProgress(cityIndex * 100f / cityCount, city))
                    val nextSkip = (key?.skip ?: 0) + LOAD_COUNT
                    syncScenicSpotData(SyncKey(city, nextSkip))
                }
            }
    }

    override suspend fun syncScenicSpotComplete() {
        _syncState.emit(SyncComplete)
    }

    override fun getLastSyncScenicSpotTime(): Long {
        return preferencesDataSource.lastSyncTime
    }

    override fun setLastSyncScenicSpotTime(t: Long) {
        preferencesDataSource.lastSyncTime = t
    }

    override fun getSyncCycleDays(): Int {
        return preferencesDataSource.syncCycleDays
    }

    private fun migrateScenicSpotCity(
        items: List<ScenicSpotEntityItem>,
        city: City
    ) {
        items.forEach { it.city = city }
    }

    private fun saveLastSyncTime() {
        preferencesDataSource.lastSyncTime = System.currentTimeMillis()
    }
}