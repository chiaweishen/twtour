package com.scw.twtour.domain

import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.entity.NoteEntity

interface NoteScenicSpotUseCase {
    suspend fun note(scenicSpotInfo: ScenicSpotInfo)
}

class NoteScenicSpotUseCaseImpl(
    private val dataSource: ScenicSpotLocalDataSource
): NoteScenicSpotUseCase {

    override suspend fun note(scenicSpotInfo: ScenicSpotInfo) {
        dataSource.insertNote(NoteEntity().update(scenicSpotInfo))
    }
}