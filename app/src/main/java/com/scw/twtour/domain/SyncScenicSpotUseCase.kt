package com.scw.twtour.domain

import com.scw.twtour.model.repository.ScenicSpotSyncingRepository
import com.scw.twtour.model.data.SyncState
import com.scw.twtour.model.repository.NetworkRepository
import com.scw.twtour.util.NetworkUtil
import kotlinx.coroutines.flow.StateFlow

interface SyncScenicSpotUseCase {
    val syncState: StateFlow<SyncState>
    suspend fun syncScenicSpotData()
}

class SyncScenicSpotUseCaseImpl(
    private val scenicSpotSyncingRepository: ScenicSpotSyncingRepository,
    private val networkRepository: NetworkRepository
) : SyncScenicSpotUseCase {

    override val syncState: StateFlow<SyncState>
        get() = scenicSpotSyncingRepository.syncState

    override suspend fun syncScenicSpotData() {
        if (needSyncing()) {
            scenicSpotSyncingRepository.syncScenicSpotData(null)
        } else {
            scenicSpotSyncingRepository.syncScenicSpotComplete()
        }
    }

    private fun needSyncing(): Boolean {
        val syncCycleTime = scenicSpotSyncingRepository.getSyncCycleDays() * 24 * 60 * 60 * 1000
        val lastSyncTime = scenicSpotSyncingRepository.getLastSyncScenicSpotTime()

        if (!networkRepository.isWifiConnected() && lastSyncTime != 0L) {
            return false
        }
        return lastSyncTime <= 0 || System.currentTimeMillis() - lastSyncTime >= syncCycleTime
    }
}