package com.scw.twtour.model.datasource.local

import com.scw.twtour.db.dao.ScenicSpotDao
import com.scw.twtour.model.entity.ScenicSpotEntityItem

interface ScenicSpotLocalDataSource {
    suspend fun cache(scenicSpots: List<ScenicSpotEntityItem>)
    suspend fun deleteAll()
}

class ScenicSpotLocalDataSourceImpl(
    private val scenicSpotDao: ScenicSpotDao
) : ScenicSpotLocalDataSource {
    override suspend fun cache(scenicSpots: List<ScenicSpotEntityItem>) {
        scenicSpotDao.insertScenicSpots(scenicSpots)
    }

    override suspend fun deleteAll() {
        scenicSpotDao.deleteAll()
    }
}