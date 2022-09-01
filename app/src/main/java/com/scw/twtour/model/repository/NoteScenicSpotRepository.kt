package com.scw.twtour.model.repository

import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.entity.NoteEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface NoteScenicSpotRepository {
    val noteState: SharedFlow<ScenicSpotInfo>
    suspend fun insertNote(scenicSpotInfo: ScenicSpotInfo)
}

class NoteScenicSpotRepositoryImpl(
    private val dataSource: ScenicSpotLocalDataSource
) : NoteScenicSpotRepository {

    private val _noteState = MutableSharedFlow<ScenicSpotInfo>(replay = 1)
    override val noteState: SharedFlow<ScenicSpotInfo>
        get() = _noteState.asSharedFlow()

    override suspend fun insertNote(scenicSpotInfo: ScenicSpotInfo) {
        dataSource.insertNote(NoteEntity().update(scenicSpotInfo))
        _noteState.tryEmit(scenicSpotInfo)
    }
}