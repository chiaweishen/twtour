package com.scw.twtour.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scw.twtour.domain.ScenicSpotUseCase
import com.scw.twtour.model.data.HomeListItem
import com.scw.twtour.model.data.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val scenicSpotUseCase: ScenicSpotUseCase
) : ViewModel() {

    private val _listItems: MutableLiveData<Result<List<HomeListItem>>> = MutableLiveData(Result.Loading)
    val listItems: LiveData<Result<List<HomeListItem>>> get() = _listItems

    fun fetchScenicSpotItems() {
        viewModelScope.launch {
            scenicSpotUseCase.fetchScenicSpotItems()
                .onStart {
                    _listItems.value = Result.Loading
                }
                .catch { e ->
                    _listItems.value = Result.Error(Exception(e))
                }
                .collect { items ->
                    _listItems.value = Result.Success(items)
                }
        }
    }
}