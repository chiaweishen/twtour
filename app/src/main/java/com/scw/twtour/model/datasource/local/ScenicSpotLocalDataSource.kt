package com.scw.twtour.model.datasource.local

import android.location.Location
import com.scw.twtour.db.dao.NoteDao
import com.scw.twtour.db.dao.ScenicSpotDao
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.entity.NoteEntity
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.constant.NoteType
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

    fun queryNotes(scenicSpotIds: Array<String>): Flow<List<NoteEntity>>

    fun queryNotes(noteType: NoteType, limit: Int?, offset: Int?): Flow<List<NoteEntity>>

    fun queryNote(scenicSpotId: String): Flow<NoteEntity?>

    suspend fun clearInvalidNote()

    suspend fun insertNote(noteEntity: NoteEntity)
}

class ScenicSpotLocalDataSourceImpl(
    private val scenicSpotDao: ScenicSpotDao,
    private val noteDao: NoteDao
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

    override fun queryNotes(scenicSpotIds: Array<String>): Flow<List<NoteEntity>> {
        return noteDao.queryNoteScenicSpots(*scenicSpotIds)
    }

    override fun queryNotes(noteType: NoteType, limit: Int?, offset: Int?): Flow<List<NoteEntity>> {
        return if (noteType == NoteType.PUSH_PIN) {
            noteDao.queryPushPinNoteScenicSpots(offset, limit)
        } else {
            noteDao.queryStarNoteScenicSpots(offset, limit)
        }
    }

    override fun queryNote(scenicSpotId: String): Flow<NoteEntity?> {
        return noteDao.queryNoteScenicSpot(scenicSpotId)
    }

    override suspend fun clearInvalidNote() {
        noteDao.clearInvalid()
    }

    override suspend fun insertNote(noteEntity: NoteEntity) {
        noteDao.insert(noteEntity)
    }
}