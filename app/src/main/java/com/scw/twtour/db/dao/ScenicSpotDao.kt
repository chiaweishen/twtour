package com.scw.twtour.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scw.twtour.modole.entity.ScenicSpotEntityItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenicSpotDao {

    @Query("SELECT * FROM scenic_spot_table LIMIT :limit")
    fun queryScenicSpots(limit: Int = -1): Flow<List<ScenicSpotEntityItem>>

    @Query("SELECT * FROM scenic_spot_table WHERE id = :id")
    fun queryScenicSpotById(id: String): Flow<ScenicSpotEntityItem>

    @Query("SELECT * FROM scenic_spot_table WHERE city = :city")
    fun queryScenicSpotsByCity(city: String): Flow<List<ScenicSpotEntityItem>>

    @Query("SELECT * FROM scenic_spot_table WHERE name LIKE '%' || :keyword || '%'")
    fun queryScenicSpotsByNameKeyword(keyword: String): Flow<List<ScenicSpotEntityItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenicSpot(scenicSpot: ScenicSpotEntityItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenicSpots(scenicSpots: List<ScenicSpotEntityItem>)

    @Query("DELETE FROM scenic_spot_table")
    suspend fun deleteAll()
}