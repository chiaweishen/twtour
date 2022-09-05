package com.scw.twtour.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenicSpotDao {

    @Query(
        "SELECT *, (ABS(:positionLat) - ABS(position_lat)) * (ABS(:positionLat) - ABS(position_lat)) " +
                "+ (ABS(:positionLon) - ABS(position_lon)) * (ABS(:positionLon) - ABS(position_lon)) as Distance " +
                "FROM scenic_spot_table ORDER BY Distance ASC LIMIT :limit"
    )
    fun queryNearbyScenicSpots(
        positionLat: Double,
        positionLon: Double,
        limit: Int = -1
    ): Flow<List<ScenicSpotEntityItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scenicSpots: List<ScenicSpotEntityItem>)

    @Query("DELETE FROM scenic_spot_table")
    suspend fun deleteAll()
}