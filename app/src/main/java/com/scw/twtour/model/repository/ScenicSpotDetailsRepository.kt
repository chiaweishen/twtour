package com.scw.twtour.model.repository

import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.datasource.remote.ScenicSpotRemoteDataSource
import com.scw.twtour.model.entity.NoteEntity
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

interface ScenicSpotDetailsRepository {
    fun fetchScenicSpotDetails(id: String): Flow<ScenicSpotInfo>
}

@FlowPreview
class ScenicSpotDetailsRepositoryImpl(
    private val remoteDataSource: ScenicSpotRemoteDataSource,
    private val localDataSource: ScenicSpotLocalDataSource
) : ScenicSpotDetailsRepository {

    override fun fetchScenicSpotDetails(id: String): Flow<ScenicSpotInfo> {
        return remoteDataSource.getScenicSpotById(id)
            .zip(fetchNote(id)) { scenicSpotItems, noteItem ->
                val scenicSpotInfo = ScenicSpotInfo()
                scenicSpotItems.firstOrNull()?.also { scenicSpotEntity ->
                    scenicSpotInfo.update(scenicSpotEntity)
                }
                noteItem?.also { noteEntity ->
                    scenicSpotInfo.update(noteEntity)
                }
                scenicSpotInfo
            }
    }

    private fun fetchNote(id: String): Flow<NoteEntity?> {
        return localDataSource.queryNote(id)
    }
}