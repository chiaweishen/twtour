package com.scw.twtour.model.repository

import android.content.Context
import com.scw.twtour.util.NetworkUtil

interface NetworkRepository {
    fun isWifiConnected(): Boolean
}

class NetworkRepositoryImpl(val context: Context) : NetworkRepository {
    override fun isWifiConnected(): Boolean {
        return NetworkUtil.isWifiConnected(context)
    }
}