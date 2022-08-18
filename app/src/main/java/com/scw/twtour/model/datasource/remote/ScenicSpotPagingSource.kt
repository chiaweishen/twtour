package com.scw.twtour.model.datasource.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.network.api.TourismApi
import com.scw.twtour.network.util.ODataFilter
import com.scw.twtour.network.util.ODataParams
import com.scw.twtour.network.util.ODataSelect
import com.scw.twtour.util.City
import timber.log.Timber

class ScenicSpotPagingSource(
    private val tourismApi: TourismApi,
    private val localDataSource: ScenicSpotLocalDataSource,
    private val city: City,
    private val zipCode: Int
) : PagingSource<Int, ScenicSpotInfo>() {

    companion object {
        const val PAGE_SIZE = 30
    }

    override fun getRefreshKey(state: PagingState<Int, ScenicSpotInfo>): Int? {
        Timber.i("refresh anchorPosition: ${state.anchorPosition}")
        state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            Timber.i("refresh position: $position, prevKey: ${page?.prevKey}, nextKey: ${page?.nextKey}")
        }
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
                            .add(ScenicSpotEntityItem::picture.name)
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
            val entities = mutableListOf<ScenicSpotEntityItem>()
            entityList.collect {
                it.forEach { entity ->
                    entity.city = city
                    entities.add(entity)
                    list.add(ScenicSpotInfo().update(entity))
                }
            }
            localDataSource.insertAll(entities)

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
        return if (ODataFilter.ScenicSpot.isOutlyingIslands(city)) {
            ODataFilter.ScenicSpot.byZipCode(zipCode)
        } else {
            ODataFilter.ScenicSpot.byCity(city)
        }
    }
}