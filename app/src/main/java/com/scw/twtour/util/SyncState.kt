package com.scw.twtour.util

sealed class SyncState

object SyncNone : SyncState()
object SyncStart : SyncState()
data class SyncProgress(val progress: Float, val city: City) : SyncState()
object SyncComplete : SyncState()
data class SyncError(val e: Exception) : SyncState()
