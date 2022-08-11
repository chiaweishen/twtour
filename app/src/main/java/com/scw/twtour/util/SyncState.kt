package com.scw.twtour.util

import com.scw.twtour.network.data.City

sealed class SyncState

object SyncNone : SyncState()
object SyncStart : SyncState()
object SyncComplete : SyncState()
data class SyncError(val e: Exception) : SyncState()
data class SyncProgress(val progress: Float, val city: City) : SyncState()