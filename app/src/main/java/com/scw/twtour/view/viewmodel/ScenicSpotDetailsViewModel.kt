package com.scw.twtour.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.NoteScenicSpotUseCase
import com.scw.twtour.domain.ScenicSpotUseCase
import com.scw.twtour.model.data.Result
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.util.ErrorUtil
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@FlowPreview
class ScenicSpotDetailsViewModel(
    private val scenicSpotUseCase: ScenicSpotUseCase,
    private val noteScenicSpotUseCase: NoteScenicSpotUseCase
) : ViewModel() {

    private val _scenicSpotInfo: MutableLiveData<Result<ScenicSpotInfo>> =
        MutableLiveData(Result.Loading)
    val scenicSpotInfo: LiveData<Result<ScenicSpotInfo>> get() = _scenicSpotInfo

    fun fetchScenicSpotItems(id: String) {
        scenicSpotUseCase.fetchScenicDetails(id)
            .onEach { info ->
                _scenicSpotInfo.value = Result.Success(info)
            }
            .catch { e ->
                ErrorUtil.networkError(e)
            }
            .launchIn(viewModelScope)
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