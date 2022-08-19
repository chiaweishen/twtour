package com.scw.twtour.domain

import com.scw.twtour.model.data.HomeListItem
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.repository.ScenicSpotDetailsRepository
import com.scw.twtour.model.repository.ScenicSpotRepository
import kotlinx.coroutines.flow.Flow

interface ScenicSpotUseCase {
    fun fetchScenicSpotItems(): Flow<List<HomeListItem>>
    fun fetchScenicDetails(id: String): Flow<ScenicSpotInfo>
}

class ScenicSpotUseCaseImpl(
    private val scenicSpotRepository: ScenicSpotRepository,
    private val scenicSpotDetailsRepository: ScenicSpotDetailsRepository
) : ScenicSpotUseCase {

    override fun fetchScenicSpotItems(): Flow<List<HomeListItem>> {
        return scenicSpotRepository.fetchItems()
    }

    override fun fetchScenicDetails(id : String): Flow<ScenicSpotInfo> {
        return scenicSpotDetailsRepository.fetchScenicSpotDetails(id)
    }
}