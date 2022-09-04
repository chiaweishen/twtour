package com.scw.twtour.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.AuthUseCase
import com.scw.twtour.domain.SyncScenicSpotUseCase
import com.scw.twtour.network.util.HeadersProvider
import com.scw.twtour.util.ErrorUtil
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@FlowPreview
class MainViewModel(
    private val authUseCase: AuthUseCase,
    private val syncScenicSpotUseCase: SyncScenicSpotUseCase,
) : ViewModel() {

    val syncState = syncScenicSpotUseCase.syncState

    fun init() {
        viewModelScope.launch {
            initAuthToken()
            syncScenicSpotData()
        }
    }

    private suspend fun initAuthToken() {
        authUseCase.getAuthToken()
            .catch { e ->
                ErrorUtil.networkError(e)
            }
            .collect {
                HeadersProvider.setAccessToken(it.accessToken)
            }
    }

    private suspend fun syncScenicSpotData() {
        syncScenicSpotUseCase.syncScenicSpotData()
    }
}