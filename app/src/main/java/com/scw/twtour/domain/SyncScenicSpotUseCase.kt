package com.scw.twtour.domain

import com.scw.twtour.model.repository.ScenicSpotRepository
import com.scw.twtour.util.SyncState
import kotlinx.coroutines.flow.StateFlow

interface SyncScenicSpotUseCase {
    val syncState: StateFlow<SyncState>
    suspend fun syncScenicSpotData()
}

class SyncScenicSpotUseCaseImpl(
    private val scenicSpotRepository: ScenicSpotRepository
) : SyncScenicSpotUseCase {

    override val syncState: StateFlow<SyncState>
        get() = scenicSpotRepository.syncState

    override suspend fun syncScenicSpotData() {
        if (needSyncing()) {
            scenicSpotRepository.syncScenicSpotData(null)
        } else {
            scenicSpotRepository.syncScenicSpotComplete()
        }
    }

    private fun needSyncing(): Boolean {
        val syncCycleTime = scenicSpotRepository.getSyncCycleDays() * 24 * 60 * 60 * 1000
        val lastSyncTime = scenicSpotRepository.getLastSyncScenicSpotTime()
        return lastSyncTime <= 0 || System.currentTimeMillis() - lastSyncTime >= syncCycleTime
    }
}