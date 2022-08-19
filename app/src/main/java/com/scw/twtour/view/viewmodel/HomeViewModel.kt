package com.scw.twtour.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.ScenicSpotUseCase
import com.scw.twtour.model.data.HomeListItem
import kotlinx.coroutines.launch

class HomeViewModel(
    private val scenicSpotUseCase: ScenicSpotUseCase
) : ViewModel() {

    private val _listItems: MutableLiveData<List<HomeListItem>> = MutableLiveData(emptyList())
    val listItems: LiveData<List<HomeListItem>> get() = _listItems

    fun fetchScenicSpotItems() {
        viewModelScope.launch {
            scenicSpotUseCase.fetchScenicSpotItems()
                .collect { items ->
                    _listItems.value = items
                }
        }
    }
}