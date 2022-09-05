package com.scw.twtour.domain

import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.repository.NoteScenicSpotRepository
import kotlinx.coroutines.flow.SharedFlow

interface NoteScenicSpotUseCase {
    suspend fun note(scenicSpotInfo: ScenicSpotInfo)
    fun scenicSpotChangedNoteState(): SharedFlow<ScenicSpotInfo>
}

class NoteScenicSpotUseCaseImpl(
    private val repository: NoteScenicSpotRepository
) : NoteScenicSpotUseCase {

    override suspend fun note(scenicSpotInfo: ScenicSpotInfo) {
        repository.insertNote(scenicSpotInfo)
    }

    override fun scenicSpotChangedNoteState(): SharedFlow<ScenicSpotInfo> {
        return repository.noteState
    }
}