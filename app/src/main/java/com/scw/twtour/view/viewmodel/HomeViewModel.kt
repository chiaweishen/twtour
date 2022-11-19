package com.scw.twtour.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.ScenicSpotUseCase
import com.scw.twtour.model.data.HomeListItem
import com.scw.twtour.model.data.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class HomeViewModel(
    private val scenicSpotUseCase: ScenicSpotUseCase
) : ViewModel() {

    private val _listItems: MutableLiveData<Result<List<HomeListItem>>> =
        MutableLiveData(Result.Loading)
    val listItems: LiveData<Result<List<HomeListItem>>> get() = _listItems

    fun fetchScenicSpotItems() {
        scenicSpotUseCase.fetchScenicSpotItems()
            .onStart {
                _listItems.value = Result.Loading
            }
            .onEach { items ->
                _listItems.value = Result.Success(items)
            }
            .catch { e ->
                _listItems.value = Result.Error(Exception(e))
            }
            .launchIn(viewModelScope)
    }
}