package com.scw.twtour.domain

import androidx.paging.PagingData
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.repository.ScenicSpotListRepository
import com.scw.twtour.constant.NoteType
import kotlinx.coroutines.flow.Flow

interface ScenicSpotNoteListUseCase {
    fun getNoteScenicSpotInfoList(noteType: NoteType): Flow<PagingData<ScenicSpotInfo>>
}

class ScenicSpotNoteListUseCaseImpl(
    private val listRepository: ScenicSpotListRepository
) : ScenicSpotNoteListUseCase {

    override fun getNoteScenicSpotInfoList(noteType: NoteType): Flow<PagingData<ScenicSpotInfo>> {
        return listRepository.getScenicSpotListByNote(noteType)
    }
}