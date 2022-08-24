package com.scw.twtour.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scw.twtour.model.data.ScenicSpotInfo

@Entity(tableName = "note_table")
data class NoteEntity(
    @PrimaryKey
    var id: String = "",
    var star: Boolean = false,
    var pin: Boolean = false
) {
    fun update(scenicSpotInfo: ScenicSpotInfo): NoteEntity {
        this.id = scenicSpotInfo.id
        this.star = scenicSpotInfo.star
        this.pin = scenicSpotInfo.pin
        return this
    }
}
