package com.scw.twtour.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.datasource.remote.ScenicSpotPagingSource
import com.scw.twtour.model.datasource.remote.ScenicSpotPagingSource.Companion.PAGE_SIZE
import com.scw.twtour.util.City
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

interface ScenicSpotListRepository : KoinComponent {
    fun getScenicSpotListByCity(city: City, zipCode: Int): Flow<PagingData<ScenicSpotInfo>>
}

class ScenicSpotListRepositoryImpl: ScenicSpotListRepository {

    override fun getScenicSpotListByCity(city: City, zipCode: Int): Flow<PagingData<ScenicSpotInfo>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { get<ScenicSpotPagingSource> { parametersOf(city, zipCode) } },
            initialKey = 1
        ).flow
    }
}