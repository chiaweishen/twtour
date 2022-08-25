package com.scw.twtour.model.repository

import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.entity.NoteEntity

interface NoteScenicSpotRepository {
    suspend fun insertNote(scenicSpotInfo: ScenicSpotInfo)
}

class NoteScenicSpotRepositoryImpl(
    private val dataSource: ScenicSpotLocalDataSource
) : NoteScenicSpotRepository {
    override suspend fun insertNote(scenicSpotInfo: ScenicSpotInfo) {
        dataSource.insertNote(NoteEntity().update(scenicSpotInfo))
    }
}