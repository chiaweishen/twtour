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
import com.scw.twtour.constant.City
import com.scw.twtour.util.CityUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber

class ScenicSpotPagingSource(
    private val tourismApi: TourismApi,
    private val localDataSource: ScenicSpotLocalDataSource,
    private val city: City,
    private val zipCode: Int,
    private val query: String
) : PagingSource<Int, ScenicSpotInfo>() {

    companion object {
        const val PAGE_SIZE = 30
    }

    override fun getRefreshKey(state: PagingState<Int, ScenicSpotInfo>): Int? {
        return state.anchorPosition?.let { position ->
            val preKey = state.closestPageToPosition(position)?.prevKey
            val nextKey = state.closestPageToPosition(position)?.nextKey
            Timber.i("getRefreshKey preKey: $preKey, nextKey: $nextKey")
            preKey?.plus(1) ?: nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ScenicSpotInfo> {
        return try {
            val list = mutableListOf<ScenicSpotInfo>()
            val position = params.key ?: 1

            val scenicSpotEntities = tourismApi.scenicSpot(
                ODataParams.Companion.Builder(PAGE_SIZE)
                    .select(
                        ODataSelect.Builder().apply {
                            add(ScenicSpotEntityItem::scenicSpotID.name)
                            add(ScenicSpotEntityItem::scenicSpotName.name)
                            add(ScenicSpotEntityItem::description.name)
                            add(ScenicSpotEntityItem::descriptionDetail.name)
                            add(ScenicSpotEntityItem::picture.name)
                            add(ScenicSpotEntityItem::zipCode.name)
                            add(ScenicSpotEntityItem::class1.name)
                            add(ScenicSpotEntityItem::class2.name)
                            add(ScenicSpotEntityItem::class3.name)
                            if (city == City.ALL) {
                                add(ScenicSpotEntityItem::address.name)
                                add(ScenicSpotEntityItem::city.name)
                            }
                        }.build()
                    )
                    .filter(getODataFilter())
                    .skip((position - 1).coerceAtLeast(0) * PAGE_SIZE)
                    .build()
            ).first() // Bad Smell

            val ids = mutableListOf<String>()
            scenicSpotEntities.forEach { scenicSpotEntity ->
                list.add(ScenicSpotInfo().update(scenicSpotEntity))
                ids.add(scenicSpotEntity.scenicSpotID)
            }

            localDataSource.clearInvalidNote()
            val noteEntities = localDataSource.queryNotes(ids.toTypedArray())
                .map { noteEntities ->
                    noteEntities.forEach { noteEntity ->
                        list.filter { it.id == noteEntity.id }.also {
                            it.firstOrNull()?.update(noteEntity)
                        }
                    }
                    list
                }
                .flowOn(Dispatchers.IO)
                .first() // Bad Smell

            noteEntities.forEach { info ->
                    if (city == City.ALL && info.city == null) {
                        info.city = CityUtil.parseAddressToCity(info.address)
                    } else {
                        info.city = city
                    }
                }

            val preKey = if (position == 1) null else position - 1
            val nextKey = if (list.size < PAGE_SIZE) null else position + 1
            Timber.i("load position: $position, preKey: $preKey, nextKey: $nextKey")
            LoadResult.Page(list, preKey, nextKey)
        } catch (e: Exception) {
            Timber.e("load error: ${e.message}")
            LoadResult.Error(e)
        }
    }

    private fun getODataFilter(): String {
        return if (city == City.ALL) {
            ODataFilter.ScenicSpot.queryByNameKeyword(query)
        } else if (isOutlyingIslands(city)) {
            ODataFilter.ScenicSpot.queryByZipCodeAndNameKeyword(zipCode, query)
        } else {
            ODataFilter.ScenicSpot.queryByCityAndNameKeyword(city, query)
        }
    }

    private fun isOutlyingIslands(city: City): Boolean {
        return city == City.XIAOLIOUCHOU || city == City.LYUDAO || city == City.LANYU
    }
}