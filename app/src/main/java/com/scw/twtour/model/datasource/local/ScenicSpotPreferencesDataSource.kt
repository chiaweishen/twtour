package com.scw.twtour.model.datasource.local

import android.content.Context

interface ScenicSpotPreferencesDataSource {
    var lastSyncTime: Long
    var syncCycleDays: Int
}

class ScenicSpotPreferencesDataSourceImpl(context: Context) : ScenicSpotPreferencesDataSource {

    private val prefs = context.getSharedPreferences("ScenicSpotPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val LAST_SYNC_TIME = "LAST_SYNC_TIME"
        private const val SYNC_CYCLE_DAYS = "SYNC_CYCLE_DAYS"
    }

    override var lastSyncTime: Long
        get() = prefs.getLong(LAST_SYNC_TIME, 0)
        set(value) {
            prefs.edit().putLong(LAST_SYNC_TIME, value).apply()
        }

    override var syncCycleDays: Int
        get() = prefs.getInt(SYNC_CYCLE_DAYS, 365) // FIXME: Syncing occurs concurrent exception!
        set(value) {
            prefs.edit().putInt(SYNC_CYCLE_DAYS, value).apply()
        }
}