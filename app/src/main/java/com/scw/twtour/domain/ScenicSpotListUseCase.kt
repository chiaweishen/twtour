package com.scw.twtour.domain

import androidx.paging.PagingData
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.repository.ScenicSpotListRepository
import com.scw.twtour.constant.City
import kotlinx.coroutines.flow.Flow

interface ScenicSpotListUseCase {
    fun getScenicSpotInfoList(
        city: City,
        zipCode: Int,
        query: String = ""
    ): Flow<PagingData<ScenicSpotInfo>>
}

class ScenicSpotListUseCaseImpl(
    private val listRepository: ScenicSpotListRepository
) : ScenicSpotListUseCase {
    override fun getScenicSpotInfoList(
        city: City,
        zipCode: Int,
        query: String
    ): Flow<PagingData<ScenicSpotInfo>> {
        return listRepository.getScenicSpotListByCity(city, zipCode, query)
    }
}