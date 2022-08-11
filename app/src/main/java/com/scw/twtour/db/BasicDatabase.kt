package com.scw.twtour.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scw.twtour.db.converter.Converters
import com.scw.twtour.db.dao.ScenicSpotDao
import com.scw.twtour.model.entity.ScenicSpotEntityItem


@Database(entities = [ScenicSpotEntityItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class BasicDatabase : RoomDatabase() {

    abstract fun scenicSpotDao(): ScenicSpotDao

    companion object {
        private var INSTANCE: BasicDatabase? = null

        fun getInstance(context: Context): BasicDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BasicDatabase::class.java,
                    "basic_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        @VisibleForTesting
        @Synchronized
        fun getTestingInstance(context: Context): BasicDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                BasicDatabase::class.java
            ).build()
        }
    }
}