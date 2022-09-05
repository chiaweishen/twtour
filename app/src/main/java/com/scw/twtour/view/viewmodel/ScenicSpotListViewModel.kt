package com.scw.twtour.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scw.twtour.domain.NoteScenicSpotUseCase
import com.scw.twtour.domain.ScenicSpotListUseCase
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.constant.City
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScenicSpotListViewModel(
    private val scenicSpotListUseCase: ScenicSpotListUseCase,
    private val noteScenicSpotUseCase: NoteScenicSpotUseCase
) : ViewModel() {

    val noteStateChanged = noteScenicSpotUseCase.scenicSpotChangedNoteState()

    fun getScenicSpotInfoList(
        city: City,
        zipCode: Int,
        query: String = ""
    ): Flow<PagingData<ScenicSpotInfo>> {
        return scenicSpotListUseCase
            .getScenicSpotInfoList(city, zipCode, query)
            .cachedIn(viewModelScope)
    }

    fun clickStar(info: ScenicSpotInfo) {
        viewModelScope.launch {
            noteScenicSpotUseCase.note(info)
        }
    }

    fun clickPushPin(info: ScenicSpotInfo) {
        viewModelScope.launch {
            noteScenicSpotUseCase.note(info)
        }
    }
}