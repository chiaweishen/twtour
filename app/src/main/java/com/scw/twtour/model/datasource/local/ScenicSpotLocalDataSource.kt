package com.scw.twtour.model.datasource.local

import com.scw.twtour.db.dao.ScenicSpotDao
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import kotlinx.coroutines.flow.Flow

interface ScenicSpotLocalDataSource {

    suspend fun cache(scenicSpots: List<ScenicSpotEntityItem>)

    suspend fun deleteAll()

    fun queryRandomScenicSpotByCity(
        cityName: String,
        limit: Int
    ): Flow<List<ScenicSpotEntityItem>>

    fun queryRandomScenicSpotsHasImageByCity(
        cityName: String,
        limit: Int
    ): Flow<List<ScenicSpotEntityItem>>

    fun queryRandomScenicSpotsHasImageInLyudao(limit: Int): Flow<List<ScenicSpotEntityItem>>

    fun queryRandomScenicSpotsHasImageInLanyu(limit: Int): Flow<List<ScenicSpotEntityItem>>

    fun queryRandomScenicSpotsHasImageInXiaoliouchou(limit: Int): Flow<List<ScenicSpotEntityItem>>
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

    override fun queryRandomScenicSpotByCity(
        cityName: String,
        limit: Int
    ): Flow<List<ScenicSpotEntityItem>> {
        return scenicSpotDao.queryRandomScenicSpotsByCity(cityName, limit)
    }

    override fun queryRandomScenicSpotsHasImageByCity(
        cityName: String,
        limit: Int
    ): Flow<List<ScenicSpotEntityItem>> {
        return scenicSpotDao.queryRandomScenicSpotsHasImageByCity(cityName, limit)
    }

    override fun queryRandomScenicSpotsHasImageInLyudao(limit: Int): Flow<List<ScenicSpotEntityItem>> {
        return scenicSpotDao.queryRandomScenicSpotsHasImageInLyudao(limit)
    }

    override fun queryRandomScenicSpotsHasImageInLanyu(limit: Int): Flow<List<ScenicSpotEntityItem>> {
        return scenicSpotDao.queryRandomScenicSpotsHasImageInLanyu(limit)
    }

    override fun queryRandomScenicSpotsHasImageInXiaoliouchou(limit: Int): Flow<List<ScenicSpotEntityItem>> {
        return scenicSpotDao.queryRandomScenicSpotsHasImageInXiaoliouchou(limit)
    }
}