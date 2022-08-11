package com.scw.twtour.network.api

import com.scw.twtour.network.util.HeadersProvider
import com.scw.twtour.network.util.ODataParams
import com.scw.twtour.model.entity.ScenicSpotEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap


interface TourismApi {
    @GET("v2/Tourism/ScenicSpot")
    fun scenicSpot(
        @QueryMap params: ODataParams,
        @HeaderMap defaultMap: Map<String, String> = HeadersProvider.get()
    ): Flow<ScenicSpotEntity>
}