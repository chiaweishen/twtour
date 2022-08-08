package com.scw.twtour.http.api

import com.scw.twtour.http.data.ODataParams
import com.scw.twtour.modole.entity.ScenicSpotEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface TourismApi {
    @GET("v2/Tourism/ScenicSpot")
    fun scenicSpot(
        @QueryMap params: ODataParams
    ): Flow<ScenicSpotEntity>
}