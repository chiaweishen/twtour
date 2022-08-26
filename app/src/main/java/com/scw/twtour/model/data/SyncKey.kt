package com.scw.twtour.model.data

import com.scw.twtour.constant.City

data class SyncKey(
    val city: City,
    val skip: Int
)
