package com.scw.twtour.model.datasource.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.network.api.TourismApi
import com.scw.twtour.network.util.ODataFilter
import com.scw.twtour.network.util.ODataParams
import com.scw.twtour.network.util.ODataSelect
import com.scw.twtour.util.City
import timber.log.Timber

class ScenicSpotPagingSource(
    private val tourismApi: TourismApi,
    private val city: City,
    private val zipCode: Int
) : PagingSource<Int, ScenicSpotInfo>() {

    companion object {
        const val PAGE_SIZE = 30
    }

    override fun getRefreshKey(state: PagingState<Int, ScenicSpotInfo>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ScenicSpotInfo> {
        return try {
            val position = params.key ?: 1
            val entityList = tourismApi.scenicSpot(
                ODataParams.Companion.Builder(PAGE_SIZE)
                    .select(
                        ODataSelect.Builder()
                            .add(ScenicSpotEntityItem::scenicSpotID.name)
                            .add(ScenicSpotEntityItem::scenicSpotName.name)
                            .add(ScenicSpotEntityItem::description.name)
                            .add(ScenicSpotEntityItem::descriptionDetail.name)
                            .add(ScenicSpotEntityItem::picture.name)
                            .add(ScenicSpotEntityItem::zipCode.name)
                            .add(ScenicSpotEntityItem::class1.name)
                            .add(ScenicSpotEntityItem::class2.name)
                            .add(ScenicSpotEntityItem::class3.name)
                            .build()
                    )
                    .filter(getODataFilter())
                    .skip((position - 1).coerceAtLeast(0) * PAGE_SIZE)
                    .build()
            )

            val list = mutableListOf<ScenicSpotInfo>()
            entityList.collect {
                it.forEach { entity ->
                    entity.city = city
                    list.add(ScenicSpotInfo().update(entity))
                }
            }

            val preKey = if (position == 1) null else position - 1
            val nextKey = if (list.isEmpty()) null else position + 1
            Timber.i("load position: $position, preKey: $preKey, nextKey: $nextKey")
            LoadResult.Page(list, preKey, nextKey)
        } catch (e: Exception) {
            Timber.e("load error: ${e.message}")
            LoadResult.Error(e)
        }
    }

    private fun getODataFilter(): String {
        return if (isOutlyingIslands(city)) {
            ODataFilter.ScenicSpot.byZipCode(zipCode)
        } else {
            ODataFilter.ScenicSpot.byCity(city)
        }
    }

    private fun isOutlyingIslands(city: City): Boolean {
        return city == City.XIAOLIOUCHOU || city == City.LYUDAO || city == City.LANYU
    }
}