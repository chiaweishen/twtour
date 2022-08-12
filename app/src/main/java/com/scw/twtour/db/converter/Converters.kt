package com.scw.twtour.db.converter

import androidx.room.TypeConverter
import com.scw.twtour.util.City
import java.util.*

class Converters {

    @TypeConverter
    fun fromString(name: String?): City? {
        return name?.let { City.valueOf(name)}
    }

    @TypeConverter
    fun stringToCity(city: City?): String? {
        return city?.let { city.name }
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}