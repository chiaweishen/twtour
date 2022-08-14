package com.scw.twtour.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.ScenicSpotUseCase
import com.scw.twtour.model.data.ScenicSpotListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val scenicSpotUseCase: ScenicSpotUseCase
) : ViewModel() {

    private val _scenicSpotItems: MutableStateFlow<List<ScenicSpotListItem>> =
        MutableStateFlow(emptyList())
    val scenicSpotItems: StateFlow<List<ScenicSpotListItem>> = _scenicSpotItems.asStateFlow()

    fun fetchScenicSpotItems() {
        viewModelScope.launch {
            scenicSpotUseCase.fetchScenicSpotItems()
                .collect { items ->
                    _scenicSpotItems.emit(items)
                }
        }
    }
}