package com.scw.twtour.model.datasource.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scw.twtour.constant.NoteType
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.network.api.TourismApi
import com.scw.twtour.network.util.ODataFilter
import com.scw.twtour.network.util.ODataParams
import com.scw.twtour.network.util.ODataSelect
import kotlinx.coroutines.flow.first
import timber.log.Timber

class NoteScenicSpotPagingSource(
    private val tourismApi: TourismApi,
    private val localDataSource: ScenicSpotLocalDataSource,
    private val noteType: NoteType
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

            localDataSource.clearInvalidNote()
            val notes = localDataSource.queryNotes(
                noteType,
                PAGE_SIZE,
                (position - 1).coerceAtLeast(0) * PAGE_SIZE
            ).first() // Bad Smell

            val ids = mutableListOf<String>().apply {
                notes.forEach {
                    add(it.id)
                }
            }

            if (ids.isEmpty()) {
                LoadResult.Page(list, null, null)
            } else {
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
                                add(ScenicSpotEntityItem::address.name)
                                add(ScenicSpotEntityItem::city.name)
                            }.build()
                        )
                        .filter(ODataFilter.ScenicSpot.queryByIdList(ids))
                        .build()
                ).first() // Bad Smell

                scenicSpotEntities.forEach { scenicSpotItem ->
                    notes.filter { it.id == scenicSpotItem.scenicSpotID }.also { noteEntities ->
                        noteEntities.firstOrNull()?.also { noteEntity ->
                            list.add(
                                ScenicSpotInfo()
                                    .update(noteEntity)
                                    .update(scenicSpotItem)
                            )
                        }
                    }
                }

                val preKey = if (position == 1) null else position - 1
                val nextKey = if (list.size < PAGE_SIZE) null else position + 1
                Timber.i("load position: $position, preKey: $preKey, nextKey: $nextKey")
                LoadResult.Page(list, preKey, nextKey)
            }
        } catch (e: Exception) {
            Timber.e("load error: ${e.message}")
            LoadResult.Error(e)
        }
    }

}