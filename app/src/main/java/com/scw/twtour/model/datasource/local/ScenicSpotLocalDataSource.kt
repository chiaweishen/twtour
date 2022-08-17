package com.scw.twtour.model.datasource.local

import android.location.Location
import com.scw.twtour.db.dao.ScenicSpotDao
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.util.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ScenicSpotLocalDataSource {

    suspend fun cache(scenicSpots: List<ScenicSpotEntityItem>)

    suspend fun deleteAll()

    fun queryRandomScenicSpotByCity(
        city: City,
        limit: Int
    ): Flow<List<ScenicSpotInfo>>

    fun queryRandomScenicSpotsHasImageByCity(
        city: City,
        limit: Int
    ): Flow<List<ScenicSpotInfo>>

    fun queryRandomScenicSpotsHasImageInLyudao(limit: Int): Flow<List<ScenicSpotInfo>>

    fun queryRandomScenicSpotsHasImageInLanyu(limit: Int): Flow<List<ScenicSpotInfo>>

    fun queryRandomScenicSpotsHasImageInXiaoliouchou(limit: Int): Flow<List<ScenicSpotInfo>>

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
        scenicSpotDao.insertScenicSpots(scenicSpots)
    }

    override suspend fun deleteAll() {
        scenicSpotDao.deleteAll()
    }

    override fun queryRandomScenicSpotByCity(
        city: City,
        limit: Int
    ): Flow<List<ScenicSpotInfo>> {
        return scenicSpotDao.queryRandomScenicSpotsByCity(city.name, limit).map { items ->
            mutableListOf<ScenicSpotInfo>().apply {
                items.forEach { item ->
                    add(ScenicSpotInfo().update(item))
                }
            }
        }
    }

    override fun queryRandomScenicSpotsHasImageByCity(
        city: City,
        limit: Int
    ): Flow<List<ScenicSpotInfo>> {
        return scenicSpotDao.queryRandomScenicSpotsHasImageByCity(city.name, limit).map { items ->
            mutableListOf<ScenicSpotInfo>().apply {
                items.forEach { item ->
                    add(ScenicSpotInfo().update(item))
                }
                if (isEmpty()) {
                    add(ScenicSpotInfo(city = city))
                }
            }
        }
    }

    override fun queryRandomScenicSpotsHasImageInLyudao(limit: Int): Flow<List<ScenicSpotInfo>> {
        return scenicSpotDao.queryRandomScenicSpotsHasImageInLyudao(limit).map { items ->
            mutableListOf<ScenicSpotInfo>().apply {
                items.forEach { item ->
                    item.city = City.LYUDAO
                    add(ScenicSpotInfo().update(item))
                }
                if (isEmpty()) {
                    add(ScenicSpotInfo(city = City.LYUDAO, zipCode = 951))
                }
            }
        }
    }

    override fun queryRandomScenicSpotsHasImageInLanyu(limit: Int): Flow<List<ScenicSpotInfo>> {
        return scenicSpotDao.queryRandomScenicSpotsHasImageInLanyu(limit).map { items ->
            mutableListOf<ScenicSpotInfo>().apply {
                items.forEach { item ->
                    item.city = City.LANYU
                    add(ScenicSpotInfo().update(item))
                }
                if (isEmpty()) {
                    add(ScenicSpotInfo(city = City.LANYU, zipCode = 952))
                }
            }
        }
    }

    override fun queryRandomScenicSpotsHasImageInXiaoliouchou(limit: Int): Flow<List<ScenicSpotInfo>> {
        return scenicSpotDao.queryRandomScenicSpotsHasImageInXiaoliouchou(limit).map { items ->
            mutableListOf<ScenicSpotInfo>().apply {
                items.forEach { item ->
                    item.city = City.XIAOLIOUCHOU
                    add(ScenicSpotInfo().update(item))
                }
                if (isEmpty()) {
                    add(ScenicSpotInfo(city = City.XIAOLIOUCHOU, zipCode = 929))
                }
            }
        }
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
                        item.position?.positionLat?:0.0,
                        item.position?.positionLon?:0.0,
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