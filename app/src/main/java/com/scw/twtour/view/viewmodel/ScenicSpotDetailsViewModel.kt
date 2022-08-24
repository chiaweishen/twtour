package com.scw.twtour.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.NoteScenicSpotUseCase
import com.scw.twtour.domain.ScenicSpotUseCase
import com.scw.twtour.model.data.ScenicSpotInfo
import kotlinx.coroutines.launch

class ScenicSpotDetailsViewModel(
    private val scenicSpotUseCase: ScenicSpotUseCase,
    private val noteScenicSpotUseCase: NoteScenicSpotUseCase
) : ViewModel() {

    private val _scenicSpotInfo: MutableLiveData<ScenicSpotInfo> = MutableLiveData(ScenicSpotInfo())
    val scenicSpotInfo: LiveData<ScenicSpotInfo> get() = _scenicSpotInfo

    fun fetchScenicSpotItems(id: String) {
        viewModelScope.launch {
            scenicSpotUseCase.fetchScenicDetails(id)
                .collect { info ->
                    _scenicSpotInfo.value = info
                }
        }
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