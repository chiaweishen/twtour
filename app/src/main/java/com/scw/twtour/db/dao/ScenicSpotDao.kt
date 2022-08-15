package com.scw.twtour.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenicSpotDao {

    @Query("SELECT * FROM scenic_spot_table LIMIT :limit")
    fun queryScenicSpots(limit: Int = -1): Flow<List<ScenicSpotEntityItem>>

    @Query("SELECT * FROM scenic_spot_table WHERE scenic_spot_id = :id")
    fun queryScenicSpotById(id: String): Flow<ScenicSpotEntityItem>

    @Query("SELECT * FROM scenic_spot_table WHERE city = :city LIMIT :limit")
    fun queryScenicSpotsByCity(city: String, limit: Int = -1): Flow<List<ScenicSpotEntityItem>>

    @Query("SELECT * FROM scenic_spot_table WHERE city = :city ORDER BY RANDOM() LIMIT :limit")
    fun queryRandomScenicSpotsByCity(
        city: String,
        limit: Int = -1
    ): Flow<List<ScenicSpotEntityItem>>

    @Query(
        "SELECT * FROM scenic_spot_table WHERE city = :city " +
                "AND (pic_url_1 NOT NULL OR pic_url_2 NOT NULL OR pic_url_3 NOT NULL) " +
                "ORDER BY RANDOM() LIMIT :limit"
    )
    fun queryRandomScenicSpotsHasImageByCity(
        city: String,
        limit: Int = -1
    ): Flow<List<ScenicSpotEntityItem>>

    @Query(
        "SELECT * FROM scenic_spot_table WHERE zip_code = '951' " +
                "AND (pic_url_1 NOT NULL OR pic_url_2 NOT NULL OR pic_url_3 NOT NULL) " +
                "ORDER BY RANDOM() LIMIT :limit"
    )
    fun queryRandomScenicSpotsHasImageInLyudao(limit: Int = -1): Flow<List<ScenicSpotEntityItem>>

    @Query(
        "SELECT * FROM scenic_spot_table WHERE zip_code = '952' " +
                "AND (pic_url_1 NOT NULL OR pic_url_2 NOT NULL OR pic_url_3 NOT NULL) " +
                "ORDER BY RANDOM() LIMIT :limit"
    )
    fun queryRandomScenicSpotsHasImageInLanyu(limit: Int = -1): Flow<List<ScenicSpotEntityItem>>

    @Query(
        "SELECT * FROM scenic_spot_table WHERE zip_code = '929' " +
                "AND (pic_url_1 NOT NULL OR pic_url_2 NOT NULL OR pic_url_3 NOT NULL) " +
                "ORDER BY RANDOM() LIMIT :limit"
    )
    fun queryRandomScenicSpotsHasImageInXiaoliouchou(limit: Int = -1): Flow<List<ScenicSpotEntityItem>>

    @Query("SELECT * FROM scenic_spot_table WHERE scenic_spot_name LIKE '%' || :keyword || '%' LIMIT :limit")
    fun queryScenicSpotsByName(
        keyword: String,
        limit: Int = -1
    ): Flow<List<ScenicSpotEntityItem>>

    @Query(
        "SELECT *, (ABS(:positionLat) - ABS(position_lat)) * (ABS(:positionLat) - ABS(position_lat)) " +
                "+ (ABS(:positionLon) - ABS(position_lon)) * (ABS(:positionLon) - ABS(position_lon)) as Distance " +
                "FROM scenic_spot_table WHERE Distance > 0 ORDER BY Distance ASC LIMIT :limit"
    )
    fun queryScenicSpotsNearBy(
        positionLat: Double,
        positionLon: Double,
        limit: Int = -1
    ): Flow<List<ScenicSpotEntityItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenicSpot(scenicSpot: ScenicSpotEntityItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenicSpots(scenicSpots: List<ScenicSpotEntityItem>)

    @Query("DELETE FROM scenic_spot_table")
    suspend fun deleteAll()
}