package com.scw.twtour.model.datasource.remote

import com.scw.twtour.network.api.TourismApi
import com.scw.twtour.network.data.City
import com.scw.twtour.network.util.ODataParams
import com.scw.twtour.network.util.ODataFilter
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import kotlinx.coroutines.flow.Flow

interface ScenicSpotRemoteDataSource {
    fun getScenicSpotsByCity(
        city: City,
        count: Int,
        skip: Int,
        select: String? = null,
        orderby: String? = null
    ): Flow<List<ScenicSpotEntityItem>>
}

class ScenicSpotRemoteDataSourceImp(
    private val tourismApi: TourismApi
) : ScenicSpotRemoteDataSource {
    override fun getScenicSpotsByCity(
        city: City,
        count: Int,
        skip: Int,
        select: String?,
        orderby: String?
    ): Flow<List<ScenicSpotEntityItem>> {
        return tourismApi.scenicSpot(
            ODataParams.Companion.Builder(count)
                .select(select)
                .skip(skip)
                .filter(ODataFilter.ScenicSpot.byCity(city))
                .orderby(orderby)
                .build()
        )
    }
}