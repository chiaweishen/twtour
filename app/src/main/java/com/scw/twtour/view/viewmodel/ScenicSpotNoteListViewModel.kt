package com.scw.twtour.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scw.twtour.domain.NoteScenicSpotUseCase
import com.scw.twtour.domain.ScenicSpotNoteListUseCase
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.constant.NoteType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ScenicSpotNoteListViewModel(
    private val scenicSpotNoteListUseCase: ScenicSpotNoteListUseCase,
    private val noteScenicSpotUseCase: NoteScenicSpotUseCase
) : ViewModel() {

    val noteStateChanged = noteScenicSpotUseCase.scenicSpotChangedNoteState()

    fun getNoteScenicSpotInfoList(noteType: NoteType): Flow<PagingData<ScenicSpotInfo>> {
        return scenicSpotNoteListUseCase
            .getNoteScenicSpotInfoList(noteType)
            .cachedIn(viewModelScope)
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