package com.scw.twtour.db.dao

import androidx.room.*
import com.scw.twtour.model.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    fun queryAllNoteScenicSpots(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE star = 1 LIMIT :limit OFFSET :offset")
    fun queryStarNoteScenicSpots(offset: Int? = 0, limit: Int? = -1): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE pin = 1 LIMIT :limit OFFSET :offset")
    fun queryPushPinNoteScenicSpots(offset: Int? = 0, limit: Int? = -1): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE id = :id")
    fun queryNoteScenicSpot(id: String): Flow<NoteEntity?>

    @Query("SELECT * FROM note_table WHERE id IN (:ids)")
    fun queryNoteScenicSpots(vararg ids: String): Flow<List<NoteEntity>>

    @Query("DELETE FROM note_table WHERE id = :id")
    suspend fun deleteNote(id: String)

    @Query("DELETE FROM note_table WHERE star = 0 AND pin = 0")
    suspend fun clearInvalid()

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)
}