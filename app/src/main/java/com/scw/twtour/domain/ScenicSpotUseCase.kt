package com.scw.twtour.domain

import com.scw.twtour.model.data.ScenicSpotListItem
import com.scw.twtour.model.repository.ScenicSpotRepository
import kotlinx.coroutines.flow.Flow

interface ScenicSpotUseCase {
    fun fetchScenicSpotItems(): Flow<List<ScenicSpotListItem>>
}

class ScenicSpotUseCaseImpl(
    private val scenicSpotRepository: ScenicSpotRepository
) : ScenicSpotUseCase {

    override fun fetchScenicSpotItems(): Flow<List<ScenicSpotListItem>> {
        return scenicSpotRepository.fetchItems()
    }
}