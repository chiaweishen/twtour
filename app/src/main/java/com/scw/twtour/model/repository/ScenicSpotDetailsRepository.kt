package com.scw.twtour.model.repository

import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.datasource.remote.ScenicSpotRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface ScenicSpotDetailsRepository {
    fun fetchScenicSpotDetails(id: String): Flow<ScenicSpotInfo>
}

class ScenicSpotDetailsRepositoryImpl(
    private val remoteDataSource: ScenicSpotRemoteDataSource
) : ScenicSpotDetailsRepository {

    override fun fetchScenicSpotDetails(id: String): Flow<ScenicSpotInfo> {
        return remoteDataSource.getScenicSpotById(id)
            .map { item ->
                item.firstOrNull()?.let {
                    ScenicSpotInfo().update(it)
                } ?: run {
                    ScenicSpotInfo()
                }
            }
            .flowOn(Dispatchers.IO)
    }
}