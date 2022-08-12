package com.scw.twtour.util

import com.scw.twtour.network.data.City

data class SyncKey(
    val city: City,
    val skip: Int
)
