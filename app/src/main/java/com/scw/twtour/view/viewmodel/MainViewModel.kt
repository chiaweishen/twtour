package com.scw.twtour.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.AuthUseCase
import com.scw.twtour.domain.SyncScenicSpotUseCase
import com.scw.twtour.network.util.HeadersProvider
import com.scw.twtour.util.ErrorUtil
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@FlowPreview
class MainViewModel(
    private val authUseCase: AuthUseCase,
    private val syncScenicSpotUseCase: SyncScenicSpotUseCase,
) : ViewModel() {

    val syncState = syncScenicSpotUseCase.syncState

    fun init() {
        syncScenicSpotData()
    }

    private fun syncScenicSpotData() {
        authUseCase.getAuthToken()
            .onEach {
                HeadersProvider.setAccessToken(it.accessToken)
            }.map {
                syncScenicSpotUseCase.syncScenicSpotData()
            }
            .catch { e ->
                ErrorUtil.networkError(e)
            }
            .launchIn(viewModelScope)
    }
}