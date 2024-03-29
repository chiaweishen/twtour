package com.scw.twtour.di.module

import com.scw.twtour.domain.*
import org.koin.dsl.module

val useCaseModule = module {
    single<AuthUseCase> { AuthUseCaseImpl(get()) }
    single<SyncScenicSpotUseCase> { SyncScenicSpotUseCaseImpl(get(), get()) }
    single<ScenicSpotUseCase> { ScenicSpotUseCaseImpl(get(), get()) }
    single<ScenicSpotListUseCase> { ScenicSpotListUseCaseImpl(get()) }
    single<NoteScenicSpotUseCase> { NoteScenicSpotUseCaseImpl(get()) }
    single<ScenicSpotNoteListUseCase> { ScenicSpotNoteListUseCaseImpl(get()) }
}