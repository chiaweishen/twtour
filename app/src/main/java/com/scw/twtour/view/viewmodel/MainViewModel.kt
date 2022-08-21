package com.scw.twtour.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.AuthUseCase
import com.scw.twtour.domain.SyncScenicSpotUseCase
import com.scw.twtour.network.util.HeadersProvider
import kotlinx.coroutines.launch

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
        authUseCase.getAuthToken().collect {
            HeadersProvider.setAccessToken(it.accessToken)
        }
    }

    private suspend fun syncScenicSpotData() {
        syncScenicSpotUseCase.syncScenicSpotData()
    }
}