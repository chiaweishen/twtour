package com.scw.twtour.di.module

import com.scw.twtour.model.repository.ScenicSpotRepository
import com.scw.twtour.model.repository.ScenicSpotRepositoryImpl
import com.scw.twtour.model.repository.ScenicSpotSyncingRepository
import com.scw.twtour.model.repository.ScenicSpotSyncingRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ScenicSpotSyncingRepository> { ScenicSpotSyncingRepositoryImpl(get(), get(), get()) }
    single<ScenicSpotRepository> { ScenicSpotRepositoryImpl(get(), get()) }
}