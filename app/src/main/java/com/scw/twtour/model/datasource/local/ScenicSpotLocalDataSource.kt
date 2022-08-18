package com.scw.twtour.model.datasource.local

import android.location.Location
import com.scw.twtour.db.dao.ScenicSpotDao
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ScenicSpotLocalDataSource {

    suspend fun cache(scenicSpots: List<ScenicSpotEntityItem>)

    suspend fun deleteAll()

    fun queryNearbyScenicSpots(
        locationLat: Double,
        locationLon: Double,
        limit: Int
    ): Flow<List<ScenicSpotInfo>>
}

class ScenicSpotLocalDataSourceImpl(
    private val scenicSpotDao: ScenicSpotDao
) : ScenicSpotLocalDataSource {

    override suspend fun cache(scenicSpots: List<ScenicSpotEntityItem>) {
        scenicSpotDao.insertAll(scenicSpots)
    }

    override suspend fun deleteAll() {
        scenicSpotDao.deleteAll()
    }

    override fun queryNearbyScenicSpots(
        locationLat: Double,
        locationLon: Double,
        limit: Int
    ): Flow<List<ScenicSpotInfo>> {
        return scenicSpotDao.queryNearbyScenicSpots(locationLat, locationLon, limit).map { items ->
            mutableListOf<ScenicSpotInfo>().apply {
                items.forEach { item ->
                    val floatArr = FloatArray(1)
                    Location.distanceBetween(
                        item.position?.positionLat ?: 0.0,
                        item.position?.positionLon ?: 0.0,
                        locationLat,
                        locationLon,
                        floatArr
                    )
                    add(ScenicSpotInfo(distanceMeter = floatArr[0].toInt()).update(item))
                }
            }
        }
    }
}