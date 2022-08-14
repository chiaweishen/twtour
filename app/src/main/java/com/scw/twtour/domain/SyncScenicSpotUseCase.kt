package com.scw.twtour.domain

import com.scw.twtour.model.repository.ScenicSpotSyncingRepository
import com.scw.twtour.util.SyncState
import kotlinx.coroutines.flow.StateFlow

interface SyncScenicSpotUseCase {
    val syncState: StateFlow<SyncState>
    suspend fun syncScenicSpotData()
}

class SyncScenicSpotUseCaseImpl(
    private val scenicSpotSyncingRepository: ScenicSpotSyncingRepository
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
        return lastSyncTime <= 0 || System.currentTimeMillis() - lastSyncTime >= syncCycleTime
    }
}