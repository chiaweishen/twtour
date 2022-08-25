package com.scw.twtour.domain

import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.entity.NoteEntity
import com.scw.twtour.model.repository.NoteScenicSpotRepository

interface NoteScenicSpotUseCase {
    suspend fun note(scenicSpotInfo: ScenicSpotInfo)
}

class NoteScenicSpotUseCaseImpl(
    private val repository: NoteScenicSpotRepository
): NoteScenicSpotUseCase {

    override suspend fun note(scenicSpotInfo: ScenicSpotInfo) {
        repository.insertNote(scenicSpotInfo)
    }
}