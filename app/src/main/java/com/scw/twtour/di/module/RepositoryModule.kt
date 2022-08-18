package com.scw.twtour.di.module

import com.scw.twtour.model.repository.*
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@FlowPreview
val repositoryModule = module {
    single<ScenicSpotSyncingRepository> { ScenicSpotSyncingRepositoryImpl(get(), get(), get()) }
    single<ScenicSpotRepository> { ScenicSpotRepositoryImpl(androidContext(), get(), get(), get()) }
    single<ScenicSpotListRepository> { ScenicSpotListRepositoryImpl() }
}