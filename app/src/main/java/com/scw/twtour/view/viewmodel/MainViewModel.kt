package com.scw.twtour.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.AuthUseCase
import com.scw.twtour.domain.SyncScenicSpotUseCase
import com.scw.twtour.network.util.HeadersProvider
import com.scw.twtour.util.AccessFineLocationDenied
import com.scw.twtour.util.AccessFineLocationGranted
import com.scw.twtour.util.AccessFineLocationNone
import com.scw.twtour.util.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val authUseCase: AuthUseCase,
    private val syncScenicSpotUseCase: SyncScenicSpotUseCase,
) : ViewModel() {

    val syncState = syncScenicSpotUseCase.syncState

    private val _permissionState = MutableStateFlow<PermissionState>(AccessFineLocationNone)
    val permissionState: StateFlow<PermissionState> get() = _permissionState.asStateFlow()

    fun init() {
        viewModelScope.launch {
            initAuthToken()
            syncScenicSpotData()
        }
    }

    fun grantAccessFineLocationPermission() {
        viewModelScope.launch {
            _permissionState.emit(AccessFineLocationGranted)
        }
    }

    fun deniedAccessFineLocationPermission() {
        viewModelScope.launch {
            _permissionState.emit(AccessFineLocationDenied)
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