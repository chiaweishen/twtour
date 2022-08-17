package com.scw.twtour.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.repository.ScenicSpotListRepository
import com.scw.twtour.util.City
import kotlinx.coroutines.flow.Flow

class ScenicSpotListViewModel(
    private val listRepository: ScenicSpotListRepository
) : ViewModel() {

    fun getScenicSpotInfoList(city: City, zipCode: Int): Flow<PagingData<ScenicSpotInfo>> {
        return listRepository.getScenicSpotListByCity(city, zipCode).cachedIn(viewModelScope)
    }
}